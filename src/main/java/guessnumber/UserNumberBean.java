package guessnumber;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@SessionScoped
public class UserNumberBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int number;
	private Integer userNumber;
	private int minimum;
	private int remainingGuesses;
	
	@Inject @MaxNumber
	private int maxNumber;
	private int maximum;
	
	@Inject @Random
	Instance<Integer> randomInt;
	
	public UserNumberBean() {}

	public int getNumber() {
		return number;
	}

	public Integer getUserNumber() {
		return userNumber;
	}

	public void setUserNumber(Integer userNumber) {
		this.userNumber = userNumber;
	}

	public int getMaximum() {
		return maximum;
	}

	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}

	public int getMinimum() {
		return minimum;
	}

	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}
	
	public int getRemainingGuesses() {
		return remainingGuesses;
	}
	
	public String check() {
		if (userNumber > number) {
			maximum = userNumber - 1;
		}
		
		if (userNumber < number) {
			minimum = userNumber + 1;
		}
		
		if (userNumber == number) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Correct!"));
		}
		
		remainingGuesses--;
		
		return null;
	}
	
	@PostConstruct
	public void reset() {
		minimum = 0;
		userNumber = 0;
		remainingGuesses = 10;
		maximum = maxNumber;
		number = randomInt.get();
	}
	
	public void validateNumberRange(FacesContext context, UIComponent toValidate, Object value) {
		if (remainingGuesses <= 0) {
			FacesMessage message = new FacesMessage("No guesses left!");
			context.addMessage(toValidate.getClientId(context), message);
			((UIInput)toValidate).setValid(false);
			
			return;
		}
		
		int input = (Integer)value;
		
		if (input < minimum || input > maximum) {
			((UIInput)toValidate).setValid(false);

			FacesMessage message = new FacesMessage("Invalid guess!");
			context.addMessage(toValidate.getClientId(context), message);
		}
	}

}
