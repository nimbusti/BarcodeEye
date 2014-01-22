package com.google.zxing.client.android.result;

import android.content.Context;
import android.net.Uri;

import com.google.zxing.Result;
import com.google.zxing.client.result.ISBNParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ProductParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.client.result.URIParsedResult;

public final class ResultHandlerFactory {

    public static ResultHandler<? extends ParsedResult> makeResultProcessor(
            Context context, Result result, Uri photoUri) {

        ParsedResult parsedResult = ResultParser.parseResult(result);

        switch (parsedResult.getType()) {
            case PRODUCT:
                return new ProductResultHandler(context,
                        (ProductParsedResult) parsedResult, result, photoUri);
            case URI:
                return new UriResultHandler(context,
                        (URIParsedResult) parsedResult, result, photoUri);
            case ISBN:
                return new IsbnResultHandler(context,
                        (ISBNParsedResult) parsedResult, result, photoUri);
            case SMS:
            case GEO:
            case TEL:
            case CALENDAR:
            case ADDRESSBOOK:
            case EMAIL_ADDRESS:
            case WIFI:
                // currently unsupported so we let them fall through
            default:
                return new TextResultHandler(context, parsedResult, result, photoUri);
        }
    }
}