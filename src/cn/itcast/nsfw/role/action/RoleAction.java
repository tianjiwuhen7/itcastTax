package cn.itcast.nsfw.role.action;


import java.util.HashSet;
import java.util.List;
 






import javax.annotation.Resource;
 
 






import cn.itcast.core.action.BaseAction;
import cn.itcast.core.content.Constant;
import cn.itcast.nsfw.role.entity.Role;
import cn.itcast.nsfw.role.entity.RolePrivilege;
import cn.itcast.nsfw.role.entity.RolePrivilegeId;
import cn.itcast.nsfw.role.service.RoleService;

import com.opensymphony.xwork2.ActionContext;
 
public class RoleAction extends BaseAction{
	@Resource
	private RoleService roleService;
	private List<Role> roleList;
	private Role role;
	private String[] privilegeIds;
	
	//列表页面
	public String listUI() throws Exception{
		try {
			//加载权限集合
			ActionContext.getContext().getContextMap().put("privilegeMap", Constant.PRIVILEGE_MAP);
			roleList=roleService.findObjects();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return "listUI";
	}
	//跳转到新增页面
	public String addUI(){
		//加载权限集合
		ActionContext.getContext().getContextMap().put("privilegeMap", Constant.PRIVILEGE_MAP);
		return "addUI";
	}
	//保存新增
	public String add(){
		if(role!=null ){
			//处理权限保存
			if(privilegeIds!=null){
				HashSet<RolePrivilege> set=new HashSet<RolePrivilege>();
				for (int i = 0; i < privilegeIds.length; i++) {
					set.add(new RolePrivilege(new RolePrivilegeId(role,privilegeIds[i])));
				}
				role.setRolePrivileges(set);
			}
			roleService.save(role);
		}
		return "list";
	}
	//跳转到编辑界面
	public String editUI(){
		//加载权限集合
		ActionContext.getContext().getContextMap().put("privilegeMap", Constant.PRIVILEGE_MAP);
		if(role!=null && role.getRoleId()!=null){
			role=roleService.findObjectById(role.getRoleId());
			//处理权限回显
			if(role.getRolePrivileges()!=null){
				privilegeIds=new String[role.getRolePrivileges().size()];
				int i=0;
				for(RolePrivilege r:role.getRolePrivileges()){
					privilegeIds[i++]=r.getId().getCode();
				}
			}
		}
		return "editUI";
	}
	//保存编辑
	public String edit(){
		
		//处理权限保存
		if(privilegeIds!=null){
			HashSet<RolePrivilege> set=new HashSet<RolePrivilege>();
			for (int i = 0; i < privilegeIds.length; i++) {
				set.add(new RolePrivilege(new RolePrivilegeId(role,privilegeIds[i])));
			}
			role.setRolePrivileges(set);
		}	
		roleService.update(role);
	
		return "list";
	}
	//删除
	public String delete(){
		if(role!=null && role.getRoleId()!=null){
			roleService.delete(role.getRoleId());
		}
		return "list";
	}
	//批量删除
	public String deleteSelected(){
		if(selectedRow!=null){
			for(String id:selectedRow){
				roleService.delete(id);
			}
		}
		return "list";
	}
	
	public List<Role> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public String[] getPrivilegeIds() {
		return privilegeIds;
	}
	public void setPrivilegeIds(String[] privilegeIds) {
		this.privilegeIds = privilegeIds;
	}
	 
}
