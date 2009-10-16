package org.imirsel.nema.dao.hibernate;

import java.util.List;

import org.imirsel.nema.dao.NotificationDao;
import org.imirsel.nema.model.Notification;


/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve Notification objects.
 */
public class NotificationDaoHibernate extends GenericDaoHibernate<Notification, Long> implements NotificationDao {

    public NotificationDaoHibernate() {
        super(Notification.class);
    }

	@SuppressWarnings("unchecked")
	public List<Notification> getNotificationsByRecipientId(Long userId) {
        return getHibernateTemplate().find("from Notification where recipientId=?", userId);
	}

}
