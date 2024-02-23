package GUI;

import jakarta.transaction.Transactional;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Category;
import models.DataGenerator;
import models.TimePeriod;
import models.money.User;
import models.instances.ExpenseInstance;
import models.instances.IncomeInstance;
import models.money.Expense;
import models.money.Income;
import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionImpl;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * JavaFX App
 */
public class App extends Application {
	
	private static StackPane root;

	@Override
	public void start(Stage stage) throws IOException {
		File db = new File("Accounts.db");
		if(!db.exists())
			DataGenerator.generate();
		FXMLLoader rootLoader = loadFXML("Root");
		root = rootLoader.load();
		var scene = new Scene(root, 600, 400);
		scene.setUserAgentStylesheet(getClass().getResource("style.css").toString());
		Parent p = loadFXML("Login").load();
		root.getChildren().add(p);
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
	
	public static void setCurrentScene(Parent parent) {
		parent.getStylesheets().clear();
		parent.getStylesheets().add(Objects.requireNonNull(App.class.getResource("style.css")).toExternalForm());
		var current = root.getChildren().getFirst();
		
		parent.translateXProperty().set(root.getWidth());
		root.getChildren().addFirst(parent);
		
		var keyValue = new KeyValue(parent.translateXProperty(), 0, Interpolator.LINEAR);
		var keyFrame = new KeyFrame(Duration.millis(500), keyValue);
		var timeline = new Timeline(keyFrame);
		timeline.setOnFinished(evt -> {
			root.getChildren().remove(current);
		});
		timeline.play();
//		scene.setRoot(parent);
//		stage.setScene(scene);
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