package com.example.androidlabs;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TestToolbar extends AppCompatActivity {

    private String overflowMenuToastMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);

        Toolbar tbar = findViewById(R.id.toolbar);
        setSupportActionBar(tbar);

        overflowMenuToastMessage = getString(R.string.menu_title_choice4_msg);
    }

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     *
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     *
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     *
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     *
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     *
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toolbar tBar = findViewById(R.id.toolbar);
        switch(item.getItemId()) {
            case R.id.menuChoice1:
                Toast.makeText(this, getString(R.string.menu_title_choice1_msg), Toast.LENGTH_LONG).show();
                break;
            case R.id.menuChoice2:
                alertExample();
                break;
            case R.id.menuChoice3:
                Snackbar sb = Snackbar.make(tBar, getString(R.string.prompt_msg), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.prompt_msg_yes), e -> finish());
                sb.show();
                break;
            case R.id.menuChoice4:
                Toast.makeText(this, overflowMenuToastMessage, Toast.LENGTH_LONG).show();
            default:
                break;
        }
        return true;
    }

    public void alertExample()
    {
        View extraStuff = getLayoutInflater().inflate(R.layout.view_extra_stuff, null);
        EditText editText = extraStuff.findViewById(R.id.editTextExtra);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("The Message")
                .setPositiveButton("Positive", (v, w) -> {
                    if (!editText.getText().toString().isEmpty())
                        overflowMenuToastMessage = editText.getText().toString();
                })
                .setNegativeButton("Negative", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Cancel
                    }
                }).setView(extraStuff);

        builder.create().show();
    }
}
