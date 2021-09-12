package com.example.ichat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PhoneLoginActivity extends AppCompatActivity {
    private Spinner spinner;
    private EditText InputPhoneNumber;
    private Button SendVerificationCodeButton;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String[] countryNames;
    private String[] countryCode;
    private int[] flags = {R.drawable.afghanisthan, R.drawable.albania, R.drawable.algeria, R.drawable.andorra, R.drawable.angola, R.drawable.antarctica, R.drawable.argentina, R.drawable.armenia, R.drawable.aruba, R.drawable.australia, R.drawable.austria, R.drawable.azerbaijan, R.drawable.bahrain, R.drawable.bangladesh, R.drawable.belarus, R.drawable.belgium, R.drawable.belize, R.drawable.benin, R.drawable.bhutan, R.drawable.bolivia, R.drawable.bosnia_and_herzegovina, R.drawable.botswana, R.drawable.brazil, R.drawable.brunei_darussalam, R.drawable.bulgaria, R.drawable.burkina_faso, R.drawable.barma, R.drawable.burundi, R.drawable.cambodia, R.drawable.cameroon, R.drawable.canada, R.drawable.cape_verde, R.drawable.central_african_republic, R.drawable.chad, R.drawable.chile, R.drawable.china, R.drawable.christmas_island, R.drawable.cocos_islands, R.drawable.colombia, R.drawable.comoros, R.drawable.congo, R.drawable.cook_islands, R.drawable.costa_rica, R.drawable.croatia, R.drawable.cuba, R.drawable.cyprus, R.drawable.czech_republic, R.drawable.denmark, R.drawable.djibouti, R.drawable.timor_leste, R.drawable.ecuador, R.drawable.egypt, R.drawable.el_salvador, R.drawable.equatorial_guinea, R.drawable.eritrea, R.drawable.estonia, R.drawable.ethiopia, R.drawable.falkland_islands, R.drawable.faroe_islands, R.drawable.fiji, R.drawable.finland, R.drawable.france, R.drawable.french_polynesia, R.drawable.gabon, R.drawable.gambia, R.drawable.georgia, R.drawable.germany, R.drawable.ghana, R.drawable.gibraltar, R.drawable.greece, R.drawable.greenland, R.drawable.guatemala, R.drawable.guinea, R.drawable.guinea_bissau, R.drawable.guyana, R.drawable.haiti, R.drawable.honduras, R.drawable.hong_kong, R.drawable.hungary, R.drawable.india, R.drawable.indonesia, R.drawable.iran, R.drawable.iraq, R.drawable.ireland, R.drawable.isle_of_man, R.drawable.israel, R.drawable.italy, R.drawable.ivory_coast, R.drawable.jamaica, R.drawable.japan, R.drawable.jordan, R.drawable.kazakhstan, R.drawable.kenya, R.drawable.kiribati, R.drawable.kuwait, R.drawable.kyrgyzstan, R.drawable.laos, R.drawable.latvia, R.drawable.lebanon, R.drawable.lesotho, R.drawable.liberia, R.drawable.libya, R.drawable.liechtenstein, R.drawable.lithuania, R.drawable.luxembourg, R.drawable.macao, R.drawable.macedonia, R.drawable.madagascar, R.drawable.malawi, R.drawable.malaysia, R.drawable.maldives, R.drawable.mali, R.drawable.malta, R.drawable.marshall_islands, R.drawable.mauritania, R.drawable.mauritius, R.drawable.mayotte, R.drawable.mexico, R.drawable.micronesia, R.drawable.moldova, R.drawable.monaco, R.drawable.mongolia, R.drawable.montenegro, R.drawable.morocco, R.drawable.mozambique, R.drawable.namibia, R.drawable.nauru, R.drawable.nepal, R.drawable.netherlands, R.drawable.new_caledonia, R.drawable.newzealand, R.drawable.nicaragua, R.drawable.niger, R.drawable.nigeria, R.drawable.niue, R.drawable.north_korea, R.drawable.norway, R.drawable.oman, R.drawable.pakistan, R.drawable.palau, R.drawable.panama, R.drawable.papua_new_guinea, R.drawable.paraguay, R.drawable.peru, R.drawable.philippines, R.drawable.pitcairn, R.drawable.poland, R.drawable.portugal, R.drawable.puerto_rico, R.drawable.qatar, R.drawable.romania, R.drawable.russian_federation, R.drawable.rwanda, R.drawable.saint_barthelemy, R.drawable.samoa, R.drawable.san_marino, R.drawable.sao_tome_and_principe, R.drawable.saudi_arabia, R.drawable.senegal, R.drawable.serbia, R.drawable.seychelles, R.drawable.sierra_leone, R.drawable.singapore, R.drawable.slovakia, R.drawable.slovenia, R.drawable.solomon_islands, R.drawable.somalia, R.drawable.south_africa, R.drawable.south_korea, R.drawable.spain, R.drawable.sri_lanka, R.drawable.saint_helena, R.drawable.saint_pierre_and_miquelon, R.drawable.sudan, R.drawable.suriname, R.drawable.swaziland, R.drawable.sweden, R.drawable.switzerland, R.drawable.syrian_arab_republic, R.drawable.taiwan, R.drawable.tajikistan, R.drawable.tanzania, R.drawable.thailand, R.drawable.togo, R.drawable.tokelau, R.drawable.tonga, R.drawable.tunisia, R.drawable.turkey, R.drawable.turkmenistan, R.drawable.tuvalu, R.drawable.united_arab_emirates, R.drawable.uganda, R.drawable.united_kingdom, R.drawable.ukraine, R.drawable.uruguay, R.drawable.united_states, R.drawable.uzbekistan, R.drawable.vanuatu, R.drawable.vatican_city_state, R.drawable.venezuela, R.drawable.vietmam, R.drawable.wallis_and_futuna, R.drawable.yemen, R.drawable.zambia, R.drawable.zimbabwe};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        spinner = findViewById(R.id.spinnerCountries);
        SendVerificationCodeButton = findViewById(R.id.send_ver_code_button);
        InputPhoneNumber = findViewById(R.id.phone_number_input);


        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        countryNames = getResources().getStringArray(R.array.country_names);
        countryCode = getResources().getStringArray(R.array.countryCode);

        CustomAdapter customAdapter = new CustomAdapter(this,flags,countryNames,countryCode);
        spinner.setAdapter(customAdapter);


        SendVerificationCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = countryCode[spinner.getSelectedItemPosition()];
                String number = InputPhoneNumber.getText().toString().trim();

                if (number.isEmpty() || number.length() < 10) {
                    InputPhoneNumber.setError("Valid number is required");
                    InputPhoneNumber.requestFocus();
                    return;
                }

                String phoneNumber = "+" + code + number;

                Intent intent = new Intent(PhoneLoginActivity.this, VerifyPhoneActivity.class);
                intent.putExtra("phonenumber", phoneNumber);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser != null) {
            Intent intent = new Intent(PhoneLoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}