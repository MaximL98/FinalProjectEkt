package controllerTests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import client.ClientController;
import common.SCCP;
import controllers.EktSystemUserLoginController;
import javafx.event.ActionEvent;
import logic.Role;
import logic.SystemUser;

public class EktSystemUserLoginControllerTests {

	private EktSystemUserLoginController loginController;
	private SystemUser systemUser;
	private Method getBtnLoginMethod;
	
	@BeforeEach
    void setUp() throws Exception {
        this.loginController = (EktSystemUserLoginController)Mockito.mock(EktSystemUserLoginController.class);
        this.systemUser = new SystemUser(123456789, "Will", "Ramos", "054-80808080", "email@email.com", null, "username", "password", Role.SUBSCRIBER);
        this.getBtnLoginMethod = EktSystemUserLoginController.class.getDeclaredMethod("getBtnLogin", ActionEvent.class);
        this.getBtnLoginMethod.setAccessible(true);
    }
	
	




}
