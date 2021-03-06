/**
 * Copyright 1996-2014 FoxBPM ORG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author kenshin
 * @author ych
 */
package org.foxbpm.engine.impl.identity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.foxbpm.engine.identity.UserDefinition;
import org.foxbpm.engine.impl.Context;
import org.foxbpm.engine.impl.db.ListQueryParameterObject;
import org.foxbpm.engine.impl.entity.UserEntity;

public class UserDefinitionImpl implements UserDefinition {
	public UserEntity findUserById(String userId) {
		return (UserEntity) Context.getCommandContext().getSqlSession().selectOne("selectUserById", userId);
	}
	
	@SuppressWarnings("unchecked")
	 
	public List<UserEntity> findUsers(String idLike, String nameLike) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (idLike != null) {
			map.put("userId", idLike);
		}
		if (nameLike != null) {
			map.put("userName", nameLike);
		}
		return (List<UserEntity>) Context.getCommandContext().getSqlSession().selectList("selectUsers", map);
	}
	
	@SuppressWarnings("unchecked")
	 
	public List<UserEntity> findUsers(String idLike, String nameLike, int pageIndex, int pageSize) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		if (idLike != null) {
			queryMap.put("userId", idLike);
		}
		if (nameLike != null) {
			queryMap.put("userName", nameLike);
		}
		int firstResult = pageIndex * pageSize - pageSize;
		int maxResults = pageSize;
		ListQueryParameterObject queryParams = new ListQueryParameterObject(queryMap, firstResult, maxResults);
		return (List<UserEntity>) Context.getCommandContext().getSqlSession().selectList("selectUsersByPage", queryParams);
	}
	
	 
	public Long findUserCount(String idLike, String nameLike) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		if (idLike != null) {
			queryMap.put("userId", idLike);
		}
		if (nameLike != null) {
			queryMap.put("userName", nameLike);
		}
		return (Long) Context.getCommandContext().getSqlSession().selectOne("selectUsersCount", queryMap);
	}
	
	 
	public void updateUser(UserEntity user) {
		Context.getCommandContext().getSqlSession().update("updateUser", user);
	}
	
	 
	public void addUser(UserEntity user) {
		Context.getCommandContext().getSqlSession().insert("insertUser", user);
	}
	
	 
	public void deleteUser(String userId) {
		Context.getCommandContext().getSqlSession().delete("insertUser", userId);
	}
}
