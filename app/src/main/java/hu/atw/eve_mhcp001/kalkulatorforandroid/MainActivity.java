package hu.atw.eve_mhcp001.kalkulatorforandroid;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import hu.atw.eve_mhcp001.kalkulatorforandroid.kalkulator.Interpreter;


public class MainActivity extends ActionBarActivity implements MemoryFragment.OnFragmentInteractionListener {
    public static final String MEMORY = "hu.awt.eve_mhcp001.kalkulatorforandroid.memory";
    public static final String INPUT_TEXT = "hu.awt.eve_mhcp001.kalkulatorforandroid.inputtext";
    private ArrayList<String> mAllowedInput;
    private EditText mInputText;
    private Interpreter mInterpreter;
    private ArrayList<String> mMemory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            mMemory = new ArrayList<String>();
        } else {
            onRestoreInstanceState(savedInstanceState);
            mMemory = savedInstanceState.getStringArrayList(MEMORY);
        }
        mInputText = (EditText) findViewById(R.id.inputText);
        mInterpreter = new Interpreter(this);
        // don't need to store these, won't change
        mAllowedInput = new ArrayList<String>();
        mAllowedInput.add("0");
        mAllowedInput.add("1");
        mAllowedInput.add("2");
        mAllowedInput.add("3");
        mAllowedInput.add("4");
        mAllowedInput.add("5");
        mAllowedInput.add("6");
        mAllowedInput.add("7");
        mAllowedInput.add("8");
        mAllowedInput.add("9");
        mAllowedInput.add("+");
        mAllowedInput.add("-");
        mAllowedInput.add("*");
        mAllowedInput.add("/");
        mAllowedInput.add("%");
        mAllowedInput.add("(");
        mAllowedInput.add(")");
        mAllowedInput.add(".");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putStringArrayList(MEMORY, mMemory);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_info) {
            Toast.makeText(this,
                    "http://www.eve-mhcp001.atw.hu\nadam.laszlo.91@gmail.com", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onButtonPressed(View view) {
        String buttonFunction = ((Button) view).getText().toString();
        // allowed inputs go straight to the edittext
        if (mAllowedInput.contains(buttonFunction)) {
            mInputText.append(buttonFunction);
            // check for function buttons here
        } else {
            String inputText = mInputText.getText().toString();
            if (buttonFunction.equals("=")) {
                if (inputText != "")
                    mInterpreter.exec(inputText);
            } else if (buttonFunction.equals("CL")) {
                mInputText.setText("");
            } else if (view.getId() == R.id.button_DEL) {
                if (inputText.length() > 0)
                    mInputText.setText(inputText.substring(0, inputText.length() - 1));
            } else if (buttonFunction.equals("MR")) {
                // add the memory fragment
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.memory_fragment_container, MemoryFragment.newInstance(mMemory));
                transaction.addToBackStack(null);
                transaction.commit();
            } else if (buttonFunction.equals("M+")) {
                if (inputText.length() > 0) {
                    mMemory.add(inputText);
                    Toast.makeText(this,
                            getResources().getString(R.string.memory_add), Toast.LENGTH_SHORT).show();
                }
            } else if (buttonFunction.equals("M-")) {
                // removes only if it exists in memory
                mMemory.remove(inputText);
                Toast.makeText(this,
                        getResources().getString(R.string.memory_rem), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Prints the answer.
     *
     * @param output The answer.
     */
    public void setOutput(Double output) {
        mInputText.setText((String.format("%6f", output)).replace(",", "."));
    }

    /**
     * Prints the error in a toast.
     *
     * @param error The error description.
     */
    public void errorOccurred(String error) {
        Toast.makeText(this,
                error, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onFragmentInteraction(String id) {
        // aw yiss, use the memory entry
        mInputText.setText(mMemory.get(Integer.parseInt(id) - 1));
    }
}
