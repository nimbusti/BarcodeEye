package com.google.zxing.client.android.result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.github.barcodeeye.CardPresenter;
import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ProductParsedResult;

import android.app.Activity;

/**
 * Handles generic products which are not books.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ProductResultHandler extends ResultHandler {

    private static final int[] buttons = {
        R.string.button_product_search,
        R.string.button_web_search,
        R.string.button_custom_product_search
    };

    public static final HashMap<String, String> PRODUCT_SEARCH_ENDPOINTS = new HashMap<String, String>();
    
    static {
      PRODUCT_SEARCH_ENDPOINTS.put("Google", "https://www.google.com/search?hl=en&tbm=shop&q={CODE}");
      PRODUCT_SEARCH_ENDPOINTS.put("Amazon", "http://www.amazon.com/s/?field-keywords={CODE}");
      PRODUCT_SEARCH_ENDPOINTS.put("eBay", "http://www.ebay.com/sch/i.html?_nkw={CODE}");
    }

    public ProductResultHandler(Activity activity, ParsedResult result, Result rawResult) {
        super(activity, result, rawResult);
    }

    @Override
    public int getButtonCount() {
      return hasCustomProductSearch() ? buttons.length : buttons.length - 1;
    }

    @Override
    public int getButtonText(int index) {
      return buttons[index];
    }

    @Override
    public void handleButtonPress(int index) {
      String productID = getProductIDFromResult(getResult());
      switch (index) {
        case 0:
          openProductSearch(productID);
          break;
        case 1:
          webSearch(productID);
          break;
        case 2:
          openURL(fillInCustomSearchURL(productID));
          break;
      }
    }

    private static String getProductIDFromResult(ParsedResult rawResult) {
      if (rawResult instanceof ProductParsedResult) {
        return ((ProductParsedResult) rawResult).getNormalizedProductID();
      }
      if (rawResult instanceof ExpandedProductParsedResult) {
        return ((ExpandedProductParsedResult) rawResult).getRawText();
      }
      throw new IllegalArgumentException(rawResult.getClass().toString());
    }
    @Override
    public int getDisplayTitle() {
      return R.string.result_product;
    }

    @Override
    public List<CardPresenter> getCardResults() {
        List<CardPresenter> cardPresenters = new ArrayList<CardPresenter>();

        ParsedResult parsedResult = getResult();
        String codeValue = parsedResult.getDisplayResult();

        for (String key : PRODUCT_SEARCH_ENDPOINTS.keySet()) {
            CardPresenter cardPresenter = new CardPresenter();
            cardPresenter.setText("Lookup on " + key).setFooter(codeValue);

            String url = PRODUCT_SEARCH_ENDPOINTS.get(key);
            url = url.replace("{CODE}", codeValue);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            cardPresenter.setPendingIntent(createPendingIntent(getActivity(), intent));

            cardPresenters.add(cardPresenter);
        }

        return cardPresenters;
    }
}
