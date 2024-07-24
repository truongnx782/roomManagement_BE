package com.example.demo.Util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

public class MapperUtils {

    // Các phương thức khác đã có ở đây...

    /**
     * Sao chép các trường có cùng tên từ đối tượng src sang đối tượng dest.
     *
     * @param src Đối tượng nguồn (class A)
     * @param dest Đối tượng đích (class B)
     * @param <A> Lớp nguồn
     * @param <B> Lớp đích
     */
    public static <A, B> void mapCommonFields(A src, B dest) {
        BeanWrapper srcWrapper = new BeanWrapperImpl(src);
        BeanWrapper destWrapper = new BeanWrapperImpl(dest);

        Set<String> commonProperties = getCommonPropertyNames(srcWrapper, destWrapper);

        BeanUtils.copyProperties(src, dest, commonProperties.toArray(new String[0]));
    }

    /**
     * Lấy các thuộc tính có cùng tên trong hai đối tượng.
     *
     * @param srcWrapper BeanWrapper của đối tượng nguồn
     * @param destWrapper BeanWrapper của đối tượng đích
     * @return Tập hợp các thuộc tính có cùng tên
     */
    private static Set<String> getCommonPropertyNames(BeanWrapper srcWrapper, BeanWrapper destWrapper) {
        Set<String> commonProperties = new HashSet<>();
        for (PropertyDescriptor srcPd : srcWrapper.getPropertyDescriptors()) {
            String propertyName = srcPd.getName();
            if (destWrapper.isWritableProperty(propertyName)) {
                commonProperties.add(propertyName);
            }
        }
        return commonProperties;
    }
}
