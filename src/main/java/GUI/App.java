package GUI;

import jakarta.transaction.Transactional;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Category;
import models.DataGenerator;
import models.TimePeriod;
import models.User;
import models.instances.ExpenseInstance;
import models.instances.IncomeInstance;
import models.money.Expense;
import models.money.Income;
import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionImpl;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * JavaFX App
 */
public class App extends Application {
	
	private static Scene scene;
	
	@Override
	public void start(Stage stage) throws IOException {
		DataGenerator.generate();
		FXMLLoader loader = loadFXML("ExpenseTable");
		Parent p = loader.load();
		ExpenseTableController controller = loader.getController();
		controller.setFields(TimePeriod.generateNewMonth());
		scene = new Scene(p, 640, 480);
		scene.setUserAgentStylesheet(getClass().getResource("style.css").toString());
		stage.setScene(scene);
		stage.show();
	}
	
	public static FXMLLoader loadFXML(String fxml) throws IOException {
		return new FXMLLoader(App.class.getResource(fxml + ".fxml"));
	}
	
	private static User currentUser;
	private static Session session;
	private static SessionFactory sessionFactory;
	
	public static SessionFactory sf() {
		return sessionFactory;
	}
	
	public static User getCurrentUser() {
		return currentUser;
	}
	
	public static void setCurrentUser(User newUser) {
		if (currentUser != null) {
			throw new IllegalStateException("Cannot overwrite logged-in user.");
		}
		currentUser = newUser;
	}
	
	public static Session s() {
		if (sessionFactory == null) {
			final Configuration configuration = new Configuration()
				.addAnnotatedClass(User.class)
				.addAnnotatedClass(Expense.class)
				.addAnnotatedClass(ExpenseInstance.class)
				.addAnnotatedClass(Income.class)
				.addAnnotatedClass(IncomeInstance.class)
				.addAnnotatedClass(TimePeriod.class)
				.addAnnotatedClass(Category.class);
//			var props = configuration.getProperties();
			sessionFactory = configuration.buildSessionFactory(
				new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()
			).build());
		}
		if(session == null || ((SessionImpl) session).isClosed()) {
			Session openedSession = sessionFactory.openSession();
			openedSession.setCacheMode(CacheMode.REFRESH);
			openedSession.setHibernateFlushMode(FlushMode.COMMIT);
			session = openedSession;
		}
		return session;
	}
	@Transactional
	public static void doWork(Consumer<Session> consumer) {
		Session sess = s();
		Transaction tx = null;
		try {
			tx = sess.beginTransaction();
		} catch(Exception none) {}
//		tx.begin();
		consumer.accept(sess);
		if(tx != null)
			tx.commit();
	}
	@Transactional
	public static void doThreadWork(Consumer<Session> consumer) {
		Session sess = sf().openSession();
		Transaction tx = null;
		try {
			tx = sess.beginTransaction();
		} catch(Exception none) {}
//		tx.begin();
		consumer.accept(sess);
		if(tx != null)
			tx.commit();
	}
	@Transactional
	public static void doNonSessionWork(Consumer<Session> consumer) {
		Session s = s();
		consumer.accept(s);
	}
	
	public static void main(String[] args) {
		launch();
	}
	
}