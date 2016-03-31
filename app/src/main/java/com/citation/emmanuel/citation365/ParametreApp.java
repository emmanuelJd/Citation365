package com.citation.emmanuel.citation365;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.citation.emmanuel.citation365.services.VerificationCitation;

import java.util.Calendar;


/**
 * Point d'entré de l'application, initialisation des différents fragments, du viewpager et du menu de navigation
 * */

public class ParametreApp extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private TextView switchNewCitationText;
    private SwitchCompat switchNewCitationSwitch;

    private TextView switchNewCitationTextVibreur;
    private SwitchCompat switchNewCitationSwitchVibreur;

    private TextView switchNewCitationTextLED;
    private SwitchCompat switchNewCitationSwitchLED;

    private TextView switchCitationTextImageDeFond;
    private SwitchCompat switchCitationSwitchImageDeFond;

    private ImageButton btn_back;

    /** variable représentant les index de stockage des options sur la notification */
    public static final String SHARENAME = "notification_citation";
    public static final String SHAREVIBREURNAME = "notification_vibreur_citation";
    public static final String SHARELEDNAME = "notification_led_citation";

    public static final String SHAREIDCITATION = "notification_id_citation";

    public static final String SHAREIMAGECITATION = "notification_image_citation";


    /** variable servant à la notification de la nouvelle citation si l'autorisation est donnée */
    public static AlarmManager alarmManager_notification = null;
    public static Intent intent_notification = null;
    public static PendingIntent pendingIntent_notification = null;


    // restart service every 1 heure
    public static final long REPEAT_TIME = 1000 * 60 * 60 * 1;

    private int notification_status = 0;
    private int notification_vibreur_status = 0;
    private int notification_led_status = 0;
    private int notification_image_fond_status = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_parametre_application);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_auteur);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
        actionBar.setDisplayHomeAsUpEnabled(true);

        /** récupération des composants de la page de paramètre */
        this.switchNewCitationText = (TextView) findViewById(R.id.new_citation_option_text);
        this.switchNewCitationSwitch = (SwitchCompat) findViewById(R.id.new_citation_option_switch);

        this.switchNewCitationTextVibreur = (TextView) findViewById(R.id.new_citation_option_text_vibration);
        this.switchNewCitationSwitchVibreur = (SwitchCompat) findViewById(R.id.new_citation_option_switch_vibration);

        this.switchNewCitationTextLED = (TextView) findViewById(R.id.new_citation_option_text_lumiere);
        this.switchNewCitationSwitchLED = (SwitchCompat) findViewById(R.id.new_citation_option_switch_lumiere);

        this.switchCitationTextImageDeFond = (TextView) findViewById(R.id.citation_option_image_de_fond);
        this.switchCitationSwitchImageDeFond = (SwitchCompat) findViewById(R.id.citation_option_switch_image_de_fond);


        /**  on récupère la valeur de l'état de l'option de notification */
        this.notification_status = getValue();
        this.notification_vibreur_status = getValueVibreur();
        this.notification_led_status = getValueLED();
        this.notification_image_fond_status = getValueImage(this);

        /** initialisation de l'état du switch de la notification en fonction de la variable*/
        if (this.notification_status != -1) {
            if (this.notification_status == 1) {
                this.switchNewCitationSwitch.setChecked(true);
            } else {
                this.switchNewCitationSwitch.setChecked(false);
            }
        } else {
            this.switchNewCitationSwitch.setChecked(false);
        }

        /** initialisation de l'état du switch de l'option vibreur en fonction de la variable*/
        if (this.notification_vibreur_status != -1) {
            if (this.notification_vibreur_status == 1) {
                this.switchNewCitationSwitchVibreur.setChecked(true);
            } else {
                this.switchNewCitationSwitchVibreur.setChecked(false);
            }
        } else {
            this.switchNewCitationSwitchVibreur.setChecked(false);
        }

        /** initialisation de l'état du switch de l'option LED en fonction de la variable*/
        if (this.notification_led_status != -1) {
            if (this.notification_led_status == 1) {
                this.switchNewCitationSwitchLED.setChecked(true);
            } else {
                this.switchNewCitationSwitchLED.setChecked(false);
            }
        } else {
            this.switchNewCitationSwitchLED.setChecked(false);
        }

        /** initialisation de l'état du switch de l'option image de fond en fonction de la variable*/
        if (this.notification_image_fond_status != -1) {
            if (this.notification_image_fond_status == 1) {
                this.switchCitationSwitchImageDeFond.setChecked(true);
            } else {
                this.switchCitationSwitchImageDeFond.setChecked(false);
            }
        } else {
            this.switchCitationSwitchImageDeFond.setChecked(false);
        }


        //attach a listener to check for changes in state
        this.switchNewCitationSwitch.setOnCheckedChangeListener(this);
        this.switchNewCitationSwitchLED.setOnCheckedChangeListener(this);
        this.switchNewCitationSwitchVibreur.setOnCheckedChangeListener(this);
        this.switchCitationSwitchImageDeFond.setOnCheckedChangeListener(this);

        //this.btn_back.setOnClickListener(this);

        setTitle("Préférences");
    }

    /** création du menu dans l'action bar */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case android.R.id.home:
                onBackPressed();
                overridePendingTransition(R.anim.slide_enter, R.anim.slide_leave);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

    /**  Getter et setter de la variable de l'état de l'option notification */
    public int getValue() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getInt(ParametreApp.SHARENAME, -1);
    }

    public void setValue(int newValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(ParametreApp.SHARENAME, newValue);
        editor.commit();
    }

    /**  Getter et setter de la variable de l'état de l'option vibreur sur la notification */
    public int getValueVibreur() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getInt(ParametreApp.SHAREVIBREURNAME, -1);
    }

    public void setValueVibreur(int newValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(ParametreApp.SHAREVIBREURNAME, newValue);
        editor.commit();
    }

    /**  Getter et setter de la variable de l'état de l'option lumière sur la notification */
    public int getValueLED() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getInt(ParametreApp.SHARELEDNAME, -1);
    }

    public void setValueLED(int newValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(ParametreApp.SHARELEDNAME, newValue);
        editor.commit();
    }


    /**  Getter et setter de la variable de l'état de l'option image de fond sur la notification */
    public static int getValueImage(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(ParametreApp.SHAREIMAGECITATION, -1);
    }

    public void setValueImage(int newValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(ParametreApp.SHAREIMAGECITATION, newValue);
        editor.commit();
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()){
            case R.id.new_citation_option_switch:
                                                    if(this.alarmManager_notification == null){
                                                        this.alarmManager_notification = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                                    }

                                                    if(this.intent_notification == null){
                                                        this.intent_notification = new Intent(ParametreApp.this, VerificationCitation.class);
                                                    }

                                                    if(this.pendingIntent_notification == null && this.intent_notification != null){
                                                        this.pendingIntent_notification = PendingIntent.getBroadcast(ParametreApp.this, 0, this.intent_notification, PendingIntent.FLAG_CANCEL_CURRENT);
                                                    }
                                                    if (isChecked) {
                                                        Calendar cal = Calendar.getInstance();

                                                        // start 30 seconds after boot completed
                                                        cal.add(Calendar.SECOND, 30);

                                                        // fetch every 30 seconds
                                                        // InexactRepeating allows Android to optimize the energy consumption
                                                        this.alarmManager_notification.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                                                                cal.getTimeInMillis(), REPEAT_TIME, this.pendingIntent_notification);

                                                        setValue(1);
                                                        Toast.makeText(this, "Notification Activée !", Toast.LENGTH_LONG).show();
                                                    } else {
                                                        this.alarmManager_notification.cancel(this.pendingIntent_notification);
                                                        setValue(0);
                                                        Toast.makeText(this, "Notification désactivée !", Toast.LENGTH_SHORT).show();
                                                    }
                                                    break;

            case R.id.new_citation_option_switch_vibration:     if (isChecked) {
                                                                    setValueVibreur(1);
                                                                } else {
                                                                    setValueVibreur(0);
                                                                }
                                                                break;

            case R.id.new_citation_option_switch_lumiere:   if (isChecked) {
                                                                setValueLED(1);
                                                            } else {
                                                                setValueLED(0);
                                                            }
                                                            break;

            case R.id.citation_option_switch_image_de_fond: if (isChecked) {
                                                                setValueImage(1);
                                                            } else {
                                                                setValueImage(0);
                                                            }
                                                                break;


            default:break;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            default :    break;
        }
    }
}
