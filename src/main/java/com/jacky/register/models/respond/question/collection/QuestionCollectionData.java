package com.jacky.register.models.respond.question.collection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jacky.register.err.qustion.notFound.QuestionNotFoundException;
import com.jacky.register.err.qustion.requireNotSatisify.RequireQuestionItemNotFillException;
import com.jacky.register.err.qustion.typeNotSupport.ItemNotSelectableException;
import com.jacky.register.err.qustion.typeNotSupport.SingleSelectException;
import com.jacky.register.models.database.quetionail.ItemType;
import com.jacky.register.models.database.quetionail.QuestionRepository;
import com.jacky.register.models.database.quetionail.collection.CollectionItem;
import com.jacky.register.models.database.quetionail.collection.CollectionItemSelect;
import com.jacky.register.models.database.quetionail.collection.QuestionCollection;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

//收集表信息
public class QuestionCollectionData {
    @JsonIgnoreProperties(allowSetters = true)
    public Integer QuestionID;
    @JsonIgnoreProperties(allowSetters = true)
    public LocalDateTime time;
    public List<Data> dataList;

    public static QuestionCollectionData fromQuestionCollection(QuestionCollection collection) {
        QuestionCollectionData data = new QuestionCollectionData();
        data.QuestionID = collection.question.ID;
        data.time = collection.submitAt;
        data.dataList = collection.items == null ? null : collection.items.stream()
                .sorted(Comparator.comparing(v -> v.item.sortIndex))
                .map(collectionItem -> {
                    Data d = new Data();
                    d.ItemID = collectionItem.item.sortIndex;
                    d.ItemInfo = collectionItem.item.item.data;
                    d.ItemSelect = collectionItem.selects.stream()
                            .map(collectionItemSelect -> {
                                        SelectData data1=new SelectData();
                                        data1.SelectItem=
                                        collectionItemSelect.select.sortIndex;
                                        data1.selectData=collectionItemSelect.value;
                                        return data1;
                                    }
                            )
                            .sorted(Comparator.comparing(selectData -> selectData.SelectItem))
                            .collect(Collectors.toList());
                    return d;
                }).collect(Collectors.toList());

        return data;
    }

    public QuestionCollection toQuestionCollection(QuestionRepository repository) {
        QuestionCollection collection = new QuestionCollection();
        var result = repository.findById(QuestionID);
        if (result.isEmpty())
            throw new QuestionNotFoundException(QuestionID);
        collection.question = result.get();
        var collectionItems = collection.question.items.stream()
                .sorted(Comparator.comparing(sort1 -> sort1.sortIndex))
                .collect(Collectors.toList());

        var requires = collection.question.items.stream()
                .filter(sort -> sort.requireFill)
                .map(v -> v.sortIndex)
                .collect(Collectors.toSet());

        collection.submitAt = LocalDateTime.now();
        //item transform
        collection.items = dataList.stream()
                //sort by id
                .sorted(Comparator.comparing(d -> d.ItemID))
                //遍历全部提交的选项
                .map(data -> {
                    CollectionItem item = new CollectionItem();
                    item.item = collectionItems.get(data.ItemID - 1);
                    item.data = data.ItemInfo;

                    //不可选择有选项
                    if (item.item.item.type != ItemType.SINGLE_CHOICE && item.item.item.type != ItemType.MULTI_CHOICE
                            && data.ItemSelect.size() > 0) {
                        throw new ItemNotSelectableException(item.item);
                    }
                    //单选提供多个选项
                    if (item.item.item.type == ItemType.SINGLE_CHOICE && data.ItemSelect.size() > 1) {
                        throw new SingleSelectException(item.item);
                    }
                    if (item.item.item.type == ItemType.SINGLE_CHOICE || item.item.item.type == ItemType.MULTI_CHOICE) {
                        //选择题选择为空就是无选项
                        if (data.ItemSelect.isEmpty()) {
                            if (item.item.requireFill)
                                throw new RequireQuestionItemNotFillException(item.item);
                        } else {
                            item.selects = data.ItemSelect.stream()
                                    .map(integer -> {

                                        var v = item.item.selects.stream()
                                                .sorted(Comparator.comparing(sort -> sort.sortIndex))
                                                .collect(Collectors.toList()).get(integer.SelectItem - 1);

                                        CollectionItemSelect select = new CollectionItemSelect();
                                        select.select = v;
                                        select.value = v.select.information;
                                        return select;
                                    }).collect(Collectors.toSet());
                        }
                    }

                    return item;
                }).collect(Collectors.toSet());

        var checkRequire = requires.stream()
                .filter(sort -> collection.items.stream()
                        .allMatch(collectionItem -> collectionItem.item.sortIndex.equals(sort)))
                .count();
        if (checkRequire > 0) {
            throw new RequireQuestionItemNotFillException(checkRequire);
        }


        return collection;
    }

    public static class Data {
        public String ItemInfo;
        public Integer ItemID;
        public List<SelectData> ItemSelect;
    }
    public static class SelectData{
        public int SelectItem;
        public String selectData;
    }
}
