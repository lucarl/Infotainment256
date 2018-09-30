package infotainment.ctrl;

import android.view.View;

import infotainment.model.Calculator;
import infotainment.view.CalculatorView;

public class CalculatorController {

    private CalculatorView calculatorView;
    private Calculator calculator;

    public CalculatorController(final CalculatorView calculatorView, final Calculator calculator) {

        this.calculatorView = calculatorView;
        this.calculator = calculator;

        this.calculatorView.addCalculationListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int firstNumber, secondNumber = 0;
                try {
                    firstNumber = calculatorView.getFirstNumber();
                    secondNumber = calculatorView.getSecondNumber();

                    calculator.addTwoNumber(firstNumber, secondNumber);

                    calculatorView.setCalcSolution(calculator.getCalculationValue());
                }
                catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
