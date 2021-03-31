package com.jacky.register.server.dbServers;

import com.jacky.register.err.NotSelectTypeItemException;
import com.jacky.register.err.RowNotFoundException;
import com.jacky.register.models.database.group.DepartmentRepository;
import com.jacky.register.models.database.group.GroupDepartment;
import com.jacky.register.models.database.quetionail.ItemType;
import com.jacky.register.models.database.quetionail.QuestionRepository;
import com.jacky.register.models.database.quetionail.Questionable;
import com.jacky.register.models.database.quetionail.choices.ItemSelectRepository;
import com.jacky.register.models.database.quetionail.choices.SelectSort;
import com.jacky.register.models.database.quetionail.choices.SelectSortRepository;
import com.jacky.register.models.database.quetionail.choices.SubItemSelect;
import com.jacky.register.models.database.quetionail.subItems.ItemRepository;
import com.jacky.register.models.database.quetionail.subItems.ItemSort;
import com.jacky.register.models.database.quetionail.subItems.ItemSortRepository;
import com.jacky.register.models.database.quetionail.subItems.QuestionSubItem;
import com.jacky.register.models.request.quesion.itemSelect.ItemSelectCreate;
import com.jacky.register.models.request.quesion.itemSelect.ItemSelectUpdate;
import com.jacky.register.models.request.quesion.item.ItemCreate;
import com.jacky.register.models.request.quesion.item.ItemUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class QuestionControlServer {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemSortRepository itemSortRepository;
    @Autowired
    ItemSelectRepository itemSelectRepository;
    @Autowired
    SelectSortRepository selectSortRepository;
    @Autowired
    DepartmentRepository departmentRepository;

    ConcurrentHashMap<UUID, ItemSort> QuestionItems = new ConcurrentHashMap<>();


    final String defaultInformation = "这是默认问卷";

    public Questionable newQuestion(GroupDepartment department, String title) {
        return newQuestion(department, title, defaultInformation);
    }

    public Questionable newQuestion(GroupDepartment department, String title, String information) {
        Questionable question = new Questionable();
        question.department = department;
        department.questions.add(question);

        question.information = information;
        question.title = title;
        question.publish = false;

        questionRepository.save(question);
        departmentRepository.save(department);

        return question;
    }

    public QuestionSubItem newQuestionItem(String information, ItemType type) {
        QuestionSubItem item = new QuestionSubItem();

        item.data = information;
        item.type = type;

        itemRepository.save(item);
        return item;
    }
    public QuestionSubItem newQuestionItem(ItemCreate create){
        return newQuestionItem(create.data.itemData,create.data.type);
    }

    public ItemSort addQuestionItem(Questionable question, QuestionSubItem item,boolean require) {
        ItemSort sort = new ItemSort();
        sort.sortIndex = question.items.size() + 1;
        sort.item = item;
        sort.question = question;
        sort.requireFill=require;
        question.items.add(sort);


        itemSortRepository.save(sort);
        itemRepository.save(item);
        questionRepository.save(question);

        return sort;
    }
    public SelectSort addItemSelect(ItemSelectCreate selectCreate) {
        var item=getItemSortByID(selectCreate.questionID, selectCreate.itemID);
        return addItemSelect(item,selectCreate.data.selectInfo,selectCreate.data.userInsert);
    }
    public SelectSort addItemSelect(ItemSort item, String selectName, boolean userInsert) {
        if (item.item.type != ItemType.MULTI_CHOICE && item.item.type != ItemType.SINGLE_CHOICE)
            throw new NotSelectTypeItemException("问卷子项类型 `" + item.item.type.name() + "` 不可添加选项");

        SubItemSelect select = new SubItemSelect();
        select.information = selectName;
        select.userInsert = userInsert;

        itemSelectRepository.save(select);

        SelectSort sort = new SelectSort();

        sort.select = select;
        sort.item=item;
        sort.sortIndex = item.selects.size() + 1;
        item.selects.add(sort);

        selectSortRepository.save(sort);
        itemSortRepository.save(item);

        return sort;
    }

    public void removeItem(Questionable question, ItemSort item) {
        question.items.remove(item);
        var temps = question.items.stream()
                .filter(sort -> sort.sortIndex >= item.sortIndex)
                .peek(sort -> sort.sortIndex = sort.sortIndex - 1)
                .collect(Collectors.toSet());

        itemSortRepository.saveAll(temps);
        itemSortRepository.delete(item);
        itemRepository.delete(item.item);
        questionRepository.save(question);
    }

    public void removeSelectItem(ItemSort item, SelectSort select) {
        item.selects.remove(select);

        var temp = item.selects.stream()
                .filter(sort -> sort.sortIndex >= select.sortIndex)
                .peek(sort -> sort.sortIndex -= 1)
                .collect(Collectors.toSet());

        selectSortRepository.saveAll(temp);
        selectSortRepository.delete(select);
        itemSelectRepository.delete(select.select);
        itemRepository.save(item.item);
    }

    public ItemSort updateItem(ItemUpdate itemUpdate) {
        var itemSort=getItemSortByID(itemUpdate.questionID, itemUpdate.itemSortID);
        var item=itemSort.item;
        var data=itemUpdate.data;

        // TODO: 2021/3/31 sort Index change

        itemSort.requireFill =data.require;
        item.type=data.type;
        item.data=data.itemData;

        itemRepository.save(item);
        itemSortRepository.save(itemSort);
        return itemSort;
    }
    public SelectSort updateSelect(ItemSelectUpdate selectUpdate){
        var selectSort=getSelectSortByID(selectUpdate.questionID,
                selectUpdate.itemID,selectUpdate.selectID);
        var select=selectSort.select;
        var data=selectUpdate.data;

        select.userInsert=data.userInsert;
        select.information= data.selectInfo;

        selectSortRepository.save(selectSort);
        itemSelectRepository.save(select);
        return selectSort;
    }
    public void publicQuestion(Integer id) {
        var q = getQuestionByID(id);
        q.publish = true;
        questionRepository.save(q);
    }

    public List<Questionable> getALL() {
        return questionRepository.findAll();
    }

    public Questionable getQuestionByID(Integer id) {
        var result = questionRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new RowNotFoundException("id==`" + id + "` not found in table Questionable");
        }
    }

    public ItemSort getItemSortByID(Questionable questionable, Integer id) {
        var result = questionable.items.stream().filter(sort -> sort.sortIndex.equals(id)).findFirst();
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new RowNotFoundException("id==`" + id + "` not found in table ItemSort");
        }
    }

    public SelectSort getItemSelectSortByID(ItemSort itemSort, Integer id) {
        var result = itemSort.selects.stream().filter(sort -> sort.sortIndex.equals(id)).findFirst();
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new RowNotFoundException("id==`" + id + "` not found in table ItemSort");
        }
    }

    public SelectSort getSelectSortByID(int questionID, int itemID, int selectID) {
        var result = selectSortRepository.findByItemQuestionIDAndItemSortIndexAndSortIndex(questionID, itemID, selectID);
        if (result.isPresent()) {
            return result.get();
        }
        throw new RowNotFoundException(String.format("questionID=%s | itemID=%s | SelectID=%s Not Found",
                questionID, itemID,
                selectID));
    }
    public ItemSort getItemSortByID(int questionID, int itemID) {
        var result = itemSortRepository.findIByQuestionIDAndSortIndex(questionID, itemID);
        if (result.isPresent()) {
            return result.get();
        }
        throw new RowNotFoundException(String.format("questionID=%s | itemID=%s  Not Found",
                questionID, itemID));
    }
}
