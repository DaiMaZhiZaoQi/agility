package com.hunt.dao;

import com.hunt.model.entity.SysLoginStatus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysLoginStatusMapper {
    //新增
    public Long insert(SysLoginStatus SysLoginStatus);

    //更新
    public void update(SysLoginStatus SysLoginStatus);

    //通过对象进行查询
    public SysLoginStatus select(SysLoginStatus SysLoginStatus);

    //通过id进行查询
    public SysLoginStatus selectById(@Param("id") Long id);

    //查询全部
    public List<SysLoginStatus> selectAll();
    
    public SysLoginStatus selectByLoginName(@Param("sysUserLoginName") String loginName);
    //查询数量
    public int selectCounts();

    SysLoginStatus selectByUserIdAndPlatform(@Param("userId") Long userId, @Param("platform") int platform);

    List<SysLoginStatus> selectByUserId(@Param("userId") long userId);
    
    /**该接口在有用户注销功能的情况下可用，无用户注销该功能就无意义*/
    List<Long> selectOnLine(List<Long> list);
    
}