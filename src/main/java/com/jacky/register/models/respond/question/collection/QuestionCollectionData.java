package com.jacky.register.models.respond.question.collection;

import com.jacky.register.err.QuestionNotFoundException;
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
    Integer QuestionID;
    LocalDateTime time;
    List<Data>dataList;

    public static QuestionCollectionData fromQuestionCollection(QuestionCollection collection){
        QuestionCollectionData data=new QuestionCollectionData();
        data.QuestionID=collection.question.ID;
        data.time=collection.submitAt;
        data.dataList=collection.items==null? null : collection.items.stream()
                .sorted(Comparator.comparing(v-> v.item.sortIndex))
                .map(collectionItem -> {
                    Data d = new Data();
                    d.ItemID=collectionItem.item.sortIndex;
                    d.ItemInfo=collectionItem.item.item.data;
                    d.ItemSelect=collectionItem.selects.stream()
                            .map(collectionItemSelect -> collectionItemSelect.select.sortIndex)
                            .collect(Collectors.toList());
                    return d;
                }).collect(Collectors.toList());

        return data;
    }

    public QuestionCollection  toQuestionCollection(QuestionRepository repository){
        QuestionCollection collection=new QuestionCollection();
        var result=repository.findById(QuestionID);
        if (result.isEmpty())
            throw new QuestionNotFoundException(QuestionID);

        collection.question=result.get();
        collection.submitAt=LocalDateTime.now();
        collection.items=dataList.stream()
                .sorted(Comparator.comparing(d->d.ItemID))
                .map(data ->{
                    CollectionItem item =new CollectionItem();
                    item.item= collection.question.items.stream()
                            .sorted(Comparator.comparing(sort1 -> sort1.sortIndex))
                            .collect(Collectors.toList()).get(data.ItemID);
                    item.data=data.ItemInfo;
                    item.selects=data.ItemSelect.stream()
                            .map(integer -> {

                                var v = item.item.item.selects.stream()
                                        .sorted(Comparator.comparing(sort -> sort.sortIndex))
                                        .collect(Collectors.toList()).get(integer);
                                CollectionItemSelect select=new CollectionItemSelect();
                                select.select=v;
                                select.value=v.select.information;
                                return select;
                            }).collect(Collectors.toSet());

                    return item;
                }).collect(Collectors.toSet());

        return collection;
    }

    static class Data{
        public String ItemInfo;
        public Integer ItemID;
        public List<Integer> ItemSelect;
    }
}
