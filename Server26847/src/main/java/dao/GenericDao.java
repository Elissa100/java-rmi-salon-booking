package dao;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import java.util.List;

public class GenericDao<T> {
    private Class<T> type;
    public GenericDao(Class<T> type) { this.type = type; }

    public T save(T obj) {
        Transaction tr = null;
        try(Session ses = HibernateUtil.getSessionFactory().openSession()){
            tr = ses.beginTransaction();
            ses.saveOrUpdate(obj);
            tr.commit();
            return obj;
        } catch(Exception e) { if(tr!=null) tr.rollback(); throw e; }
    }

    public List<T> findAll() {
        try(Session ses = HibernateUtil.getSessionFactory().openSession()){
            return ses.createQuery("from " + type.getName(), type).list();
        }
    }

    public void delete(Integer id) {
        Transaction tr = null;
        try(Session ses = HibernateUtil.getSessionFactory().openSession()){
            tr = ses.beginTransaction();
            T obj = ses.get(type, (java.io.Serializable) id);
            if(obj != null) ses.delete(obj);
            tr.commit();
        } catch(Exception e) { if(tr!=null) tr.rollback(); throw e; }
    }
}