package hmdm.mapper;

import hmdm.dto.Role;
import hmdm.dto.RoleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RoleMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table hmdm_pt_role
	 * @mbg.generated  Wed Aug 09 11:45:39 GMT+08:00 2017
	 */
	long countByExample(RoleExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table hmdm_pt_role
	 * @mbg.generated  Wed Aug 09 11:45:39 GMT+08:00 2017
	 */
	int deleteByExample(RoleExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table hmdm_pt_role
	 * @mbg.generated  Wed Aug 09 11:45:39 GMT+08:00 2017
	 */
	int deleteByPrimaryKey(Integer roleId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table hmdm_pt_role
	 * @mbg.generated  Wed Aug 09 11:45:39 GMT+08:00 2017
	 */
	int insert(Role record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table hmdm_pt_role
	 * @mbg.generated  Wed Aug 09 11:45:39 GMT+08:00 2017
	 */
	int insertSelective(Role record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table hmdm_pt_role
	 * @mbg.generated  Wed Aug 09 11:45:39 GMT+08:00 2017
	 */
	List<Role> selectByExample(RoleExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table hmdm_pt_role
	 * @mbg.generated  Wed Aug 09 11:45:39 GMT+08:00 2017
	 */
	Role selectByPrimaryKey(Integer roleId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table hmdm_pt_role
	 * @mbg.generated  Wed Aug 09 11:45:39 GMT+08:00 2017
	 */
	int updateByExampleSelective(@Param("record") Role record, @Param("example") RoleExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table hmdm_pt_role
	 * @mbg.generated  Wed Aug 09 11:45:39 GMT+08:00 2017
	 */
	int updateByExample(@Param("record") Role record, @Param("example") RoleExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table hmdm_pt_role
	 * @mbg.generated  Wed Aug 09 11:45:39 GMT+08:00 2017
	 */
	int updateByPrimaryKeySelective(Role record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table hmdm_pt_role
	 * @mbg.generated  Wed Aug 09 11:45:39 GMT+08:00 2017
	 */
	int updateByPrimaryKey(Role record);
}