module se233.chapter5part22 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires org.slf4j;


    opens se233.chapter5part22 to javafx.fxml;
    exports se233.chapter5part22;
}