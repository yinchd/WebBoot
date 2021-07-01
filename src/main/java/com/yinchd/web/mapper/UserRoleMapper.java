package com.yinchd.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yinchd.web.model.UserRoleModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户角色映射表 Mapper 接口
 * </p>
 *
 * @author yinchd
 * @since 2021-03-18
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRoleModel> {

}
