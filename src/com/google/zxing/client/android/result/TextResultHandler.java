package com.google.zxing.client.android.result;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;

import com.github.barcodeeye.CardPresenter;
import com.google.zxing.Result;
import com.github.barcodeeye.R;
import com.google.zxing.client.result.ParsedResult;

import android.app.Activity;

/**
 * This class handles TextParsedResult as well as unknown formats. It's the fallback handler.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class TextResultHandler extends ResultHandler {

    private static final int[] buttons = {
        R.string.button_web_search,
        R.string.button_share_by_email,
        R.string.button_share_by_sms,
        R.string.button_custom_product_search,
    };

    private static final String SEARCH_URL = "https://www.google.com/search?q=%s";

    public TextResultHandler(Activity activity, ParsedResult result, Result rawResult) {
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
      String text = getResult().getDisplayResult();
      switch (index) {
        case 0:
          webSearch(text);
          break;
        case 1:
          shareByEmail(text);
          break;
        case 2:
          shareBySMS(text);
          break;
        case 3:
          openURL(fillInCustomSearchURL(text));
          break;
      }
    }

    @Override
    public int getDisplayTitle() {
      return R.string.result_text;
    }

    @Override
    public List<CardPresenter> getCardResults() {
        List<CardPresenter> cardPresenters = new ArrayList<CardPresenter>();

        ParsedResult parsedResult = getResult();
        String codeValue = parsedResult.getDisplayResult();

        CardPresenter cardPresenter = new CardPresenter();
        cardPresenter.setText("Search Web").setFooter(codeValue);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(String.format(SEARCH_URL, codeValue)));
        cardPresenter.setPendingIntent(createPendingIntent(getActivity(), intent));

        cardPresenters.add(cardPresenter);

        return cardPresenters;
    }
}
