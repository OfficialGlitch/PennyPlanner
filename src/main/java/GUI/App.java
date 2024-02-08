package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Category;
import models.TimePeriod;
import models.User;
import models.instances.ExpenseInstance;
import models.instances.IncomeInstance;
import models.money.Expense;
import models.money.Income;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * JavaFX App
 */
public class App extends Application {
	
	private static Scene scene;
	
	@Override
	public void start(Stage stage) throws IOException {
		scene = new Scene(loadFXML("primary"), 640, 480);
		stage.setScene(scene);
		stage.show();
	}
	
	static void setRoot(String fxml) throws IOException {
		scene.setRoot(loadFXML(fxml));
	}
	
	public static Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
		return fxmlLoader.load();
	}
	
	private static User currentUser;
	
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
			sessionFactory = configuration.buildSessionFactory(new StandardServiceRegistryBuilder().build());
		}
		Session session = sessionFactory.openSession();
		session.setHibernateFlushMode(FlushMode.ALWAYS);
		
		return session;
	}
	
	public static void doWork(Consumer<Session> consumer) {
		Session sess = s();
		Transaction tx = sess.getTransaction();
		tx.begin();
		consumer.accept(sess);
		tx.commit();
	}
	
	public static void main(String[] args) {
		launch();
	}
	
}