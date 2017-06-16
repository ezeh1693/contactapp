package com.contact.utils;

import com.contact.dao.Accessor;
import com.contact.dao.Param;
import com.contact.models.Model;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by emmanuel on 12/15/15.
 */
public class Utility {


    public static Map<String, String> errors(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getDefaultMessage());
        });
        return errors;
    }

    public static Map<String, String> getRequestParams(HttpServletRequest r){
        Map<String, String> params = new HashMap<>();
        Enumeration<String> e = r.getParameterNames();
        while(e.hasMoreElements()){
            String name = e.nextElement();
            params.put(name, r.getParameter(name));
        }
        return params;
    }

    public static Param getParam(HttpServletRequest req){
        Param p = new Param(0, 50);
        String page = req.getParameter("page");
        if(StringUtils.isNumeric(page)){
            p.setPage(Integer.valueOf(page));
        }
        String size = req.getParameter("size");
        if(StringUtils.isNumeric(size)){
            p.setSize(Integer.valueOf(size));
        }
        String sort = req.getParameter("sort");
        p.setSort("created DESC");//Default;
        if(StringUtils.isNotEmpty(sort)){
            p.setSort(sort);
        }
        return p;
    }

    /**
     * Utility Method to be used for manipulating most form of updates
     * from a form input to be persisted on the database.
     * This method relies heavily on Reflection, Generic, Collection, and the new Optional Interface.
     * @param formModel - input model representing all or some field of an Entity Model
     * @param oldModel - Entity model from the database that requires update
     * @param <T> - a Generic Type, that extends a com.emmanuel.models.Model as an Upper Bound
     * @return - T - a generic Model to be persisted.
     */
    public static <T extends Model> T updateModel(Optional<T> formModel, Optional<T> oldModel) {
        T modelForm = formModel.get();
        //Get the actual Class of this Entity (instead of the generic model class)
        Class<?> clazz = modelForm.getClass();
        //Get all the declared fields in this class
        List<Field> fields = Arrays.asList(clazz.getDeclaredFields());

        fields.forEach(f -> {
            //Convert the first Letter of this field to Capital(JavaBean Style)
            String fieldName = WordUtils.capitalize(f.getName());
            //Get the actual Class Type of this field instead of its direct superclass or interface
            Class<?> fieldClass = f.getType();

            try {
                //Declared getter method of this field
                Method getMethod = clazz.getDeclaredMethod("get" + fieldName, null);
                //Value of this field, returned via reflection from the getter method
                Object fieldValue = getMethod.invoke(modelForm, null);

                if (null != fieldValue) {
                    //Declared setter method of this field, fieldClass is the only Parameter type its taking
                    Method setMethod = clazz.getDeclaredMethod("set" + fieldName, fieldClass);
                    setMethod.invoke(oldModel.get(), fieldValue);
                }

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        });

        return oldModel.get();
    }

    public static <T extends Model> void updateModel(Long id, Class<T> clazz, String field, Object value) {

        T mm = Accessor.findOne(clazz, id);
        if (mm != null) {
            try {
                Field f = clazz.getField(field);
                String fieldName = WordUtils.capitalize(f.getName());
                //Get the actual Class Type of this field instead of its direct superclass or interface
                Class<?> fieldClass = f.getType();

                //Declared getter method of this field
                Method getMethod = clazz.getDeclaredMethod("get" + fieldName, null);
                //Value of this field, returned via reflection from the getter method
                Object fieldValue = value;//getMethod.invoke(modelForm, null);

                if (null != fieldValue) {
                    //Declared setter method of this field, fieldClass is the only Parameter type its taking
                    Method setMethod = clazz.getDeclaredMethod("set" + fieldName, fieldClass);
                    setMethod.invoke(mm, fieldValue);
                }

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

            Accessor.saveOne(mm);
        }
    }

    public static double divide(double dividend, double divisor) {
        if (divisor == 0) {
            return 0.0;
        }
        return dividend / divisor;
    }


}
