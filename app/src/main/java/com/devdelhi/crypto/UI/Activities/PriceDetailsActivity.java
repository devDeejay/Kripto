package com.devdelhi.crypto.UI.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.devdelhi.crypto.Data.Coins;
import com.devdelhi.crypto.Data.Currency;
import com.devdelhi.crypto.R;

public class PriceDetailsActivity extends AppCompatActivity {

    private Coins[] finalReceivedCoinDataArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_details);

        for (int i = 0 ; i < finalReceivedCoinDataArray.length; i++){
            Coins cryptoCoin = finalReceivedCoinDataArray[i];
            Currency[] userCurrencyArray = cryptoCoin.getDataArray();

            for (int j = 0 ; j < userCurrencyArray.length ; j++){
                Currency currencyDetails = userCurrencyArray[j];

                long price = currencyDetails.getPRICE();
                String market = currencyDetails.getMARKET();
                String fromSymbol = currencyDetails.getFROMSYMBOL();
                //String tosymbol = currencyDetails.getTOSYMBOL();
                long lastupdate = currencyDetails.getLASTUPDATE();
                String lastmarket = currencyDetails.getLASTMARKET();
                long volumeday = currencyDetails.getVOLUMEDAY();
                long volumedayto = currencyDetails.getVOLUMEDAYTO();
                long volume24HOUR = currencyDetails.getVOLUME24HOUR();
                long volume24HOURTO = currencyDetails.getVOLUME24HOURTO();
                long open24HOUR = currencyDetails.getOPEN24HOUR();
                long openday = currencyDetails.getOPENDAY();
                long lowday = currencyDetails.getLOWDAY();
                long highday = currencyDetails.getHIGHDAY();
                long high24HOUR = currencyDetails.getHIGH24HOUR();
                long low24HOUR = currencyDetails.getLOW24HOUR();
                long change24HOUR = currencyDetails.getCHANGE24HOUR();
                long changepct24HOUR = currencyDetails.getCHANGEPCT24HOUR();
                long changeday = currencyDetails.getCHANGEDAY();
                long changepctday = currencyDetails.getCHANGEPCTDAY();
                long supply = currencyDetails.getSUPPLY();
                long mktcap = currencyDetails.getMKTCAP();
                long totalvolume24H = currencyDetails.getTOTALVOLUME24H();
                long totalvolume24HTO = currencyDetails.getTOTALVOLUME24HTO();
                long lastvolume = currencyDetails.getLASTVOLUME();
                long lastvolumeto = currencyDetails.getLASTVOLUMETO();
                //double tradeID = currencyDetails.getLASTTRADEID();
            }
        }
    }
}
