package com.cg.citymanage.models;

import java.util.List;

/**
/*                       _oo0oo_
/*                      o8888888o
/*                      88" . "88
/*                      (| -_- |)
/*                      0\  =  /0
/*                    ___/`---'\___
/*                  .' \\|     |// '.
/*                 / \\|||  :  |||// \
/*                / _||||| -:- |||||- \
/*               |   | \\\  -  /// |   |
/*               | \_|  ''\---/''  |_/ |
/*               \  .-\__  '-'  ___/-. /
/*             ___'. .'  /--.--\  `. .'___
/*          ."" '<  `.___\_<|>_/___.' >' "".
/*         | | :  `- \`.;`\ _ /`;.`/ - ` : | |
/*         \  \ `_.   \_ __\ /__ _/   .-` /  /
/*     =====`-.____`.___ \_____/___.-`___.-'=====
/*                       `=---='
/*
/*     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*               佛祖保佑         永无BUG

* 功能：部件统计列表model
* 作者：cg
* 时间：2019/11/14 15:08
*/
public class PartListModel {

    private String unitTypeId;	    //id	是	[string]
    private String unitTypeName;	//名称	是	[string]
    private String totalCount;	    //数量	是	[string]
    private List<PartListModel> children;

    public PartListModel() {
    }

    public String getUnitTypeId() {
        return unitTypeId;
    }

    public void setUnitTypeId(String unitTypeId) {
        this.unitTypeId = unitTypeId;
    }

    public String getUnitTypeName() {
        return unitTypeName;
    }

    public void setUnitTypeName(String unitTypeName) {
        this.unitTypeName = unitTypeName;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public List<PartListModel> getChildren() {
        return children;
    }

    public void setChildren(List<PartListModel> children) {
        this.children = children;
    }
}
