package com.android.bidbatl.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Comparator;
import java.util.List;

public class Category {

    public int code;
    public String message;
    @SerializedName("data")
    public List<CategoryList> categoryLists;
    public static class CategoryList{
        public String id;
        public String parent_id;
        public String name;
        public String category_image;
        public String status;
        public String category_name;

        public CategoryList(String id, String name) {
            this.id = id;
            this.name = name;
        }
        /*Comparator for sorting the list by Student Name*/
        public static Comparator<CategoryList> StuNameComparator = new Comparator<CategoryList>() {

            public int compare(CategoryList s1, CategoryList s2) {
                String StudentName1 = s1.name.toUpperCase();
                String StudentName2 = s2.name.toUpperCase();

                //ascending order
                return StudentName1.compareTo(StudentName2);

                //descending order
                //return StudentName2.compareTo(StudentName1);
            }};

    }
}
