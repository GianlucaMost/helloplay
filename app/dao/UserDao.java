package dao;

import java.util.Collection;

import models.Tipp;
import models.User;

public interface UserDao {
	public void add(String name, String pwHash);
	public void delete(User u);
    public User findByName(String name);
    public Collection<User> findAll();
    public Collection<Tipp> findSortedTipps(User u);
	public boolean userExist(String name);
	public void update(User u, String name, String pwHash);
    public void changeName(User u, String name);
    public void changePw(User u, String pwHash);
    public boolean validate(String name, String pw);
}
