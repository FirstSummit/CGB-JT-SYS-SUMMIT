package mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import mybatis.po.SysUsers;
import mybatis.po.SysUsersExample;

public interface SysUsersMapper {
    int countByExample(SysUsersExample example);

    int deleteByExample(SysUsersExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SysUsers record);

    int insertSelective(SysUsers record);

    List<SysUsers> selectByExample(SysUsersExample sysUsersExample);

    SysUsers selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SysUsers record, @Param("example") SysUsersExample example);

    int updateByExample(@Param("record") SysUsers record, @Param("example") SysUsersExample example);

    int updateByPrimaryKeySelective(SysUsers record);

    int updateByPrimaryKey(SysUsers record);
}