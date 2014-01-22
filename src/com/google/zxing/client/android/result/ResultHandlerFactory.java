package com.google.zxing.client.android.result;

import android.content.Context;
import android.net.Uri;

import com.google.zxing.Result;
import com.google.zxing.client.result.ISBNParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ProductParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.client.result.URIParsedResult;

/**
 * Manufactures Android-specific handlers based on the barcode content's type.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ResultHandlerFactory {
    private ResultHandlerFactory() {
    }

    public static ResultHandler<? extends ParsedResult> makeResultHandler(Context context, Result rawResult) {//, Uri photoUri) {

        ParsedResult result = ResultParser.parseResult(rawResult);

        switch (result.getType()) {
            case PRODUCT:
                return new ProductResultHandler(context, (ProductParsedResult) result, rawResult);//, photoUri);
            case URI:
                return new URIResultHandler(context, (URIParsedResult) result, rawResult);//, photoUri);
            case ISBN:
                return new ISBNResultHandler(context, (ISBNParsedResult) result, rawResult);//, photoUri);
            case WIFI:
            case GEO:
            case TEL:
            case SMS:
            case CALENDAR:
            case ADDRESSBOOK:
            case EMAIL_ADDRESS:
                // currently unsupported so we let them fall through
            default:
                return new TextResultHandler(context, result, rawResult);//, photoUri);
        }
    }
    private static ParsedResult parseResult(Result rawResult) {
      return ResultParser.parseResult(rawResult);
    }
}
