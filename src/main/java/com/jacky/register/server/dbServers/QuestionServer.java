package com.jacky.register.server.dbServers;

import com.jacky.register.err.NotSelectTypeItemException;
import com.jacky.register.err.RowNotFound;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class QuestionServer {
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

    public ItemSort addQuestionItem(Questionable question, QuestionSubItem item) {
        ItemSort sort = new ItemSort();
        sort.sortIndex = question.items.size() + 1;
        sort.item = item;
        sort.question = question;

        question.items.add(sort);


        itemSortRepository.save(sort);
        itemRepository.save(item);
        questionRepository.save(question);

        return sort;
    }

    public SelectSort addItemSelect(QuestionSubItem item, String selectName, boolean userInsert) {
        if (item.type != ItemType.MULTI_CHOICE && item.type != ItemType.SINGLE_CHOICE)
            throw new NotSelectTypeItemException("问卷子项类型 `" + item.type.name() + "` 不可添加选项");

        SubItemSelect select = new SubItemSelect();
        select.information = selectName;
        select.userInsert = userInsert;

        itemSelectRepository.save(select);

        SelectSort sort = new SelectSort();

        sort.select = select;
        sort.sortIndex = item.selects.size() + 1;
        item.selects.add(sort);

        selectSortRepository.save(sort);
        itemRepository.save(item);

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

    public void removeSelectItem(ItemSort item,SelectSort select){
        item.item.selects.remove(select);

        var temp=item.item.selects.stream()
                .filter(sort -> sort.sortIndex>=select.sortIndex)
                .peek(sort -> sort.sortIndex-=1)
                .collect(Collectors.toSet());

        selectSortRepository.saveAll(temp);
        selectSortRepository.delete(select);
        itemSelectRepository.delete(select.select);
        itemRepository.save(item.item);
    }

    public List<Questionable> getALL() {
        return questionRepository.findAll();
    }

    public Questionable getQuestionByID(Integer id) {
        var result = questionRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new RowNotFound("id==`" + id + "` not found in table Questionable");
        }
    }

    public ItemSort getItemSortByID(Questionable questionable, Integer id) {
        var result = questionable.items.stream().filter(sort -> sort.sortIndex.equals(id)).findFirst();
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new RowNotFound("id==`" + id + "` not found in table ItemSort");
        }
    }

    public SelectSort getItemSelectSortByID(ItemSort itemSort, Integer id) {
        var result = itemSort.item.selects.stream().filter(sort -> sort.sortIndex.equals(id)).findFirst();
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new RowNotFound("id==`" + id + "` not found in table ItemSort");
        }
    }
}
