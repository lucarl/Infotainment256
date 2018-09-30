package infotainment.model;

public class Calculator {

    private int calculationValue;

    public void addTwoNumber(int firstNumber, int secondNumber) {

        calculationValue = firstNumber + secondNumber;

    }

    public int getCalculationValue() {
        return calculationValue;
    }
}
