package com.google.zxing.client.android.result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.github.barcodeeye.CardPresenter;
import com.github.barcodeeye.R;
import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.result.ISBNParsedResult;
import com.google.zxing.client.result.ParsedResult;

public final class ISBNResultHandler extends ResultHandler {
    public static final HashMap<String, String> PRODUCT_SEARCH_ENDPOINTS = new HashMap<String, String>();

    static {
        PRODUCT_SEARCH_ENDPOINTS.put("Google", "https://www.google.com/search?hl=en&tbm=shop&q={CODE}");
        PRODUCT_SEARCH_ENDPOINTS.put("Amazon", "http://www.amazon.com/s/?field-keywords={CODE}");
        PRODUCT_SEARCH_ENDPOINTS.put("eBay", "http://www.ebay.com/sch/i.html?_nkw={CODE}");
    }

  private static final int[] buttons = {
      R.string.button_product_search,
      R.string.button_book_search,
      R.string.button_search_book_contents,
      R.string.button_custom_product_search
  };

    public ISBNResultHandler(Activity activity, ParsedResult result, Result rawResult) {
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
    ISBNParsedResult isbnResult = (ISBNParsedResult) getResult();
    switch (index) {
      case 0:
        openProductSearch(isbnResult.getISBN());
        break;
      case 1:
        openBookSearch(isbnResult.getISBN());
        break;
      case 2:
        searchBookContents(isbnResult.getISBN());
        break;
      case 3:
        openURL(fillInCustomSearchURL(isbnResult.getISBN()));
        break;
    }
  }

  @Override
  public int getDisplayTitle() {
    return R.string.result_isbn;
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
