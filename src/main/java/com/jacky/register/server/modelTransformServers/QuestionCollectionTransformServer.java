package com.jacky.register.server.modelTransformServers;

import com.jacky.register.err.qustion.notFound.ItemSelectNotFoundException;
import com.jacky.register.err.qustion.requireNotSatisify.RequireQuestionItemNotFillException;
import com.jacky.register.err.qustion.requireNotSatisify.RequireUserFillItemSelectionException;
import com.jacky.register.err.qustion.typeNotSupport.*;
import com.jacky.register.models.database.quetionail.ItemType;
import com.jacky.register.models.database.quetionail.choices.SelectSort;
import com.jacky.register.models.database.quetionail.collection.CollectionItem;
import com.jacky.register.models.database.quetionail.collection.CollectionItemRepository;
import com.jacky.register.models.database.quetionail.collection.CollectionItemSelect;
import com.jacky.register.models.database.quetionail.collection.QuestionCollection;
import com.jacky.register.models.database.quetionail.subItems.ItemSort;
import com.jacky.register.models.respond.question.collection.QuestionCollectionData;
import com.jacky.register.server.dbServers.qustion.QuestionControlServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 问卷收集表的服务
 * 将收集表转换成数据库表
 */
@Service
public class QuestionCollectionTransformServer {
    @Autowired
    QuestionControlServer controlServer;
    @Autowired
    CollectionItemRepository itemRepository;

    public static final Pattern numberPattern =
            Pattern.compile("^-?(?:(?:0x[0-9a-f]+)|(?:0b[01]+)|(?:0o[0-7]+)|(?:\\d+(?:\\.\\d+)))$");
    public static final Pattern emailPattern =
            Pattern.compile("^([a-zA-Z0-9]+([-|.])?)+@([a-zA-Z0-9]+(-[a-zA-Z0-9]+)?\\.)+[a-zA-Z]{2,}$");


    public com.jacky.register.models.database.quetionail.collection.QuestionCollection toQuestionCollection(
            QuestionCollectionData data,
            int questionID) {
        QuestionCollection questionCollection = new QuestionCollection();
        var question = controlServer.getQuestionByID(questionID);
        var pushTime = LocalDateTime.now();

        questionCollection.question = question;
        questionCollection.submitAt = pushTime;

        questionCollection.items = toQuestionItems(data.dataList, question.items);
        return questionCollection;
    }

    private Set<CollectionItem> toQuestionItems(List<QuestionCollectionData.Data> dataList, Set<ItemSort> items) {
        Set<CollectionItem> itemSet = new HashSet<>();

        HashMap<Integer, QuestionCollectionData.Data> dataHashMap = new HashMap<>();
        for (QuestionCollectionData.Data data :
                dataList) {
            dataHashMap.put(data.ItemID, data);
        }

        var sortedItem = items.stream()
                .sorted(Comparator.comparing(itemSort -> itemSort.sortIndex))
                .collect(Collectors.toList());

        for (ItemSort item :
                sortedItem) {
            var itemId = item.sortIndex;
            if (dataHashMap.containsKey(itemId)) {
                QuestionCollectionData.Data data = dataHashMap.get(itemId);
                CollectionItem collectionItem = new CollectionItem();

                collectionItem.item = item;
                // TODO: 2021/4/4 select add
                collectionItem.selects = toItemSelects(data.ItemSelect, item);

                //必填内容却为null 或者空字符串
                if (item.requireFill && (
                        (item.item.type == ItemType.TEXT ||
                                item.item.type == ItemType.TEXT_AREA ||
                                item.item.type == ItemType.EMAIL ||
                                item.item.type == ItemType.PASSWORD ||
                                item.item.type == ItemType.NUMBER
                        ) && (data.ItemInfo == null || data.ItemInfo.equals(""))))
                    throw new RequireQuestionItemNotFillException(item);
                //检查邮箱格式
                if (item.item.type == ItemType.EMAIL && !emailPattern.matcher(data.ItemInfo).matches()) {
                    throw new ItemTypePatternNotMatchException(data.ItemInfo, item.item.type);
                }
                //检查数字格式
                if (item.item.type == ItemType.NUMBER && !numberPattern.matcher(data.ItemInfo).matches()) {
                    throw new ItemTypePatternNotMatchException(data.ItemInfo, item.item.type);
                }
                //检查长度
                if (data.ItemInfo.length() > 128) {
                    throw new ItemDataLengthOverLimitationException(128, data.ItemInfo.length(), item);
                }
                // 唯一性检查 | 数据库交互
                if (item.uniqueItem && itemRepository.countByDataAndItem(data.ItemInfo, item) > 0) {
                    throw new UniqueItemRepeatException(item, data.ItemInfo);
                }

                collectionItem.data = data.ItemInfo;

                itemSet.add(collectionItem);
            } else {
                //必须填
                if (item.requireFill)
                    throw new RequireQuestionItemNotFillException(item);
            }
        }
        return itemSet;
    }

    private Set<CollectionItemSelect> toItemSelects(List<QuestionCollectionData.SelectData> SelectIndex, ItemSort sort) {
        Set<CollectionItemSelect> itemSelectSet = new HashSet<>();
        //类型检查
        if (!(sort.item.type == ItemType.SINGLE_CHOICE ||
                sort.item.type == ItemType.MULTI_CHOICE ||
                sort.item.type == ItemType.CHECK_BOX)) {
            if (!SelectIndex.isEmpty()) {
                throw new ItemNotSelectableException(sort);
            } else
                return itemSelectSet;
        }
        //可选填充判定
        if (sort.requireFill && SelectIndex.isEmpty())
            throw new RequireQuestionItemNotFillException(sort);

        //单选里面有多选
        if (sort.item.type == ItemType.SINGLE_CHOICE && SelectIndex.size() != 1)
            throw new SingleSelectException(sort);
        //chekBOX 变成单选或者多选
        // 长度为1，第一个为0（未选择）或者1（选择）
        if (sort.item.type == ItemType.CHECK_BOX && (SelectIndex.size() != 1 &&
                (SelectIndex.get(0).SelectItem != 0 || SelectIndex.get(0).SelectItem != 1)))
            throw new CheckBoxItemWithUnexpectedSelectIdException(sort);
        //构建映射
        HashMap<Integer, SelectSort> selectSortHashMap = new HashMap<>();
        for (SelectSort selectSort :
                sort.selects) {
            selectSortHashMap.put(selectSort.sortIndex, selectSort);
        }

        for (QuestionCollectionData.SelectData index :
                SelectIndex) {
            //选项存在性判断
            if (!selectSortHashMap.containsKey(index.SelectItem))
                throw new ItemSelectNotFoundException(sort, index.SelectItem);

            CollectionItemSelect select = new CollectionItemSelect();
            var selectSort = selectSortHashMap.get(index.SelectItem);
            select.select = selectSort;

            //用户填充项判定
            if (selectSort.select.userInsert) {
                //空白填充
                if (index.selectData == null || index.selectData.length() == 0)
                    throw new RequireUserFillItemSelectionException(sort, selectSort);
                //填充过长
                if (index.selectData.length() > 32)
                    throw new UserFillSelectLengthOverLimitationException(sort, selectSort, index.selectData.length());

                select.value = index.selectData;
            }
            itemSelectSet.add(select);
        }
        return itemSelectSet;
    }
}
