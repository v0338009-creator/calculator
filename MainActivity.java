package com.example.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Color;
import android.text.InputType;
import android.widget.Toast;

public class MainActivity extends Activity {

    private EditText display;
    private String operator = "";
    private double firstOperand = 0;
    private boolean isNewInput = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(20, 20, 20, 20);
        root.setBackgroundColor(Color.parseColor("#1E1E1E"));

        display = new EditText(this);
        display.setTextSize(36);
        display.setTextColor(Color.WHITE);
        display.setGravity(android.view.Gravity.END);
        display.setInputType(InputType.TYPE_NULL);
        display.setText("0");
        LinearLayout.LayoutParams dp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 2f);
        root.addView(display, dp);

        String[][] buttons = {
                {"C", "÷", "×", "-"},
                {"7", "8", "9", "+"},
                {"4", "5", "6", "="},
                {"1", "2", "3", "="},
                {"0", ".", "=", "="}
        };

        for (int r = 0; r < buttons.length; r++) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f);
            root.addView(row, rowParams);

            for (int c = 0; c < buttons[r].length; c++) {
                final String label = buttons[r][c];

                // Пропускаем дубли "=" — они уже заняты
                if (label.equals("=") && !(c == 3 && (r == 2 || r == 4))) {
                    LinearLayout spacer = new LinearLayout(this);
                    row.addView(spacer, new LinearLayout.LayoutParams(0, 0, 1f));
                    continue;
                }

                Button btn = new Button(this);
                btn.setText(label);
                btn.setTextSize(20);
                btn.setTextColor(Color.WHITE);
                btn.setBackgroundColor(label.equals("=")
                        ? Color.parseColor("#FF9500")
                        : Color.parseColor("#333333"));

                LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                        0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
                btnParams.setMargins(6, 6, 6, 6);
                btn.setLayoutParams(btnParams);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onButtonClick(label);
                    }
                });

                row.addView(btn);
            }
        }

        setContentView(root);
    }

    private void onButtonClick(String label) {
        if (label.equals("C")) {
            display.setText("0");
            operator = "";
            firstOperand = 0;
            isNewInput = true;
            return;
        }

        if (label.equals("+") || label.equals("-") || label.equals("×") || label.equals("÷")) {
            String cur = display.getText().toString();
            if (!cur.isEmpty() && !cur.equals("Ошибка")) {
                firstOperand = Double.parseDouble(cur);
                operator = label;
                isNewInput = true;
            }
            return;
        }

        if (label.equals("=")) {
            calculate();
            return;
        }

        // цифры и точка
        if (isNewInput) {
            display.setText("");
            isNewInput = false;
        }
        String cur = display.getText().toString();
        if (label.equals(".") && cur.contains(".")) return;
        if (cur.equals("0") && !label.equals(".")) {
            display.setText(label);
        } else {
            display.setText(cur + label);
        }
    }

    private void calculate() {
        if (operator.isEmpty()) return;
        String cur = display.getText().toString();
        if (cur.isEmpty() || cur.equals("Ошибка")) return;

        double second = Double.parseDouble(cur);
        double result = 0;

        switch (operator) {
            case "+": result = firstOperand + second; break;
            case "-": result = firstOperand - second; break;
            case "×": result = firstOperand * second; break;
            case "÷":
                if (second == 0) {
                    display.setText("Ошибка");
                    operator = "";
                    isNewInput = true;
                    return;
                }
                result = firstOperand / second;
                break;
        }

        String out = (result == (long) result)
                ? String.valueOf((long) result)
                : String.valueOf(result);

        display.setText(out);
        operator = "";
        isNewInput = true;
    }
}
