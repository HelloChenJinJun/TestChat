package org.pointstone.cugappplat.base.basemvp;

/**
 * 项目名称:    Cugappplat
 * 创建人:        陈锦军
 * 创建时间:    2017/4/5      17:35
 * QQ:             1981367757
 */

public interface ILoadDataView<T> extends BaseView{
        void updateData(T data);
}
