package com.mycompany.aiverse_application;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itis.libs.parserng.android.expressParser.MathExpression;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class CalculatorFragment extends Fragment {
    private String currentNumber;
    public CalculatorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_calculator, container, false);
        currentNumber = "";
        Button zero = rootView.findViewById(R.id.zero);
        Button one = rootView.findViewById(R.id.one);
        Button two = rootView.findViewById(R.id.two);
        Button three = rootView.findViewById(R.id.three);
        Button four = rootView.findViewById(R.id.four);
        Button five = rootView.findViewById(R.id.five);
        Button six = rootView.findViewById(R.id.six);
        Button seven = rootView.findViewById(R.id.seven);
        Button eight = rootView.findViewById(R.id.eight);
        Button nine = rootView.findViewById(R.id.nine);
        Button reset = rootView.findViewById(R.id.reset);
        Button decimal = rootView.findViewById(R.id.decimal_point);
        Button add = rootView.findViewById(R.id.add);
        Button minus = rootView.findViewById(R.id.minus);
        Button equals = rootView.findViewById(R.id.equals);
        TextView textView = rootView.findViewById(R.id.numbers);
        reset.setOnClickListener(v -> {
            currentNumber = "";
            textView.setText(currentNumber);
        });
        zero.setOnClickListener(v -> {
            currentNumber += "0";
            textView.setText(currentNumber);
        });
        one.setOnClickListener(v -> {
            currentNumber += "1";
            textView.setText(currentNumber);
        });
        two.setOnClickListener(v -> {
            currentNumber += "2";
            textView.setText(currentNumber);
        });
        three.setOnClickListener(v -> {
            currentNumber += "3";
            textView.setText(currentNumber);
        });
        four.setOnClickListener(v -> {
            currentNumber += "4";
            textView.setText(currentNumber);
        });
        five.setOnClickListener(v -> {
            currentNumber += "5";
            textView.setText(currentNumber);
        });
        six.setOnClickListener(v -> {
            currentNumber += "6";
            textView.setText(currentNumber);
        });
        seven.setOnClickListener(v -> {
            currentNumber += "7";
            textView.setText(currentNumber);
        });
        eight.setOnClickListener(v -> {
            currentNumber += "8";
            textView.setText(currentNumber);
        });
        nine.setOnClickListener(v -> {
            currentNumber += "9";
            textView.setText(currentNumber);
        });
        add.setOnClickListener(v -> {
            currentNumber += "+";
            textView.setText(currentNumber);
        });
        minus.setOnClickListener(v -> {
            currentNumber += "-";
            textView.setText(currentNumber);
        });
        decimal.setOnClickListener(v -> {
            currentNumber += ".";
            textView.setText(currentNumber);
        });
        equals.setOnClickListener(v -> {
            if (currentNumber.equals("1234")){
                NavDirections action = CalculatorFragmentDirections.actionCalculatorFragmentToLoginFragment();
                Navigation.findNavController(rootView).navigate(action);
            }
            else{
                MathExpression expr = new MathExpression(currentNumber);
                textView.setText(expr.solve());
                currentNumber = "";
            }
        });
        return rootView;
    }

}
