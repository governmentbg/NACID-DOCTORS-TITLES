package com.nacid.bl.menu;

import com.nacid.bl.users.User;

public interface MenuDataProvider {
    
    public Menu constructMenuForUser(User user);

	public int saveMenu(int id, int parentId, String name, String longName, int ordNum, boolean active);

	public void deleteMenu(int id);
}
