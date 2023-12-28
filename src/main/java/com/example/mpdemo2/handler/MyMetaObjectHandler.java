package com.example.mpdemo2.handler;/**
 * @description:
 * @author: Administrator
 * @time: 2023/12/22 0022 16:11
 */

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @description:
 * @author: Administrator
 * @time: 2023/12/22 0022 16:11
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject,"conditionDate", Date.class,new Date());
        this.strictInsertFill(metaObject,"resttime", Date.class,new Date());
        metaObject.setValue("deleteFlag",0);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject,"resttime", Date.class,new Date());
    }
}
