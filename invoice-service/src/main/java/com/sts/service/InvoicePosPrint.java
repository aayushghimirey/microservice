package com.sts.service;


import com.sts.domain.BusinessDetailResponse;
import com.sts.model.Invoice;
import com.sts.model.InvoiceItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class InvoicePosPrint {

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.systemDefault());

    private InvoicePosPrint() {
    }

    public static String printInvoice(Invoice invoice, BusinessDetailResponse businessDetail) {

        List<InvoiceItem> items = invoice.getItems();

        List<InvoiceItem> printableItems = items.stream()
                .filter(InvoiceItem::isPrintable)
                .toList();

        BigDecimal nonPrintableTotal = items.stream()
                .filter(item -> !item.isPrintable())
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        boolean hasNonPrintable = nonPrintableTotal.compareTo(BigDecimal.ZERO) > 0;

        String printedAt = invoice.getCreatedDateTime() != null
                ? DATE_FMT.format(invoice.getCreatedDateTime())
                : DATE_FMT.format(java.time.Instant.now());

        // ── Item rows ────────────────────────────────────────
        StringBuilder itemRows = new StringBuilder();
        for (InvoiceItem item : printableItems) {
            BigDecimal lineTotal = item.getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()))
                    .setScale(2, RoundingMode.HALF_UP);
            itemRows.append("<tr>")
                    .append("<td class=\"item-name\">").append(escHtml(item.getName())).append("</td>")
                    .append("<td class=\"item-qty\">").append(formatQty(item.getQuantity())).append("</td>")
                    .append("<td class=\"item-price\">").append(fmt(item.getPrice())).append("</td>")
                    .append("<td class=\"item-total\">").append(fmt(lineTotal)).append("</td>")
                    .append("</tr>");
        }
        if (hasNonPrintable) {
            itemRows.append("<tr>")
                    .append("<td class=\"item-name unknown\">Unknown</td>")
                    .append("<td class=\"item-qty\">-</td>")
                    .append("<td class=\"item-price\">-</td>")
                    .append("<td class=\"item-total\">")
                    .append(fmt(nonPrintableTotal.setScale(2, RoundingMode.HALF_UP)))
                    .append("</td>")
                    .append("</tr>");
        }

        // ── Optional meta rows ───────────────────────────────
        StringBuilder metaRows = new StringBuilder();

        // Invoice type (NEW field: BillingType invoiceType)
        if (invoice.getInvoiceType() != null) {
            metaRows.append("<tr><td>Type</td><td>")
                    .append(escHtml(invoice.getInvoiceType().name()))
                    .append("</td></tr>");
        }
        if (invoice.getReservationTime() != null) {
            metaRows.append("<tr><td>Reservation</td><td>")
                    .append(DATE_FMT.format(invoice.getReservationTime()))
                    .append("</td></tr>");
        }
        if (notBlank(businessDetail.getPanNumber())) {
            metaRows.append("<tr><td>PAN</td><td>")
                    .append(escHtml(businessDetail.getPanNumber()))
                    .append("</td></tr>");
        }

        // ── Discount row ─────────────────────────────────────
        String discountRow = "";
        if (invoice.getDiscountAmount() != null
                && invoice.getDiscountAmount().compareTo(BigDecimal.ZERO) > 0) {
            discountRow = "<tr><td>Discount</td><td>- " + fmt(invoice.getDiscountAmount()) + "</td></tr>";
        }

        // ── Footer extras ────────────────────────────────────
        StringBuilder footerExtra = new StringBuilder();
        if (notBlank(businessDetail.getPanNumber())) {
            footerExtra.append("<div>PAN: ").append(escHtml(businessDetail.getPanNumber())).append("</div>");
        }
        if (notBlank(businessDetail.getBusinessEmail())) {
            footerExtra.append("<div>").append(escHtml(businessDetail.getBusinessEmail())).append("</div>");
        }

        // ── Contact line ─────────────────────────────────────
        StringBuilder contactLine = new StringBuilder();
        if (notBlank(businessDetail.getBusinessNumber())) {
            contactLine.append("Tel: ").append(escHtml(businessDetail.getBusinessNumber()));
        }
        if (notBlank(businessDetail.getBusinessEmail())) {
            if (!contactLine.isEmpty()) contactLine.append(" | ");
            contactLine.append(escHtml(businessDetail.getBusinessEmail()));
        }

        String paymentMethod = invoice.getPaymentMethod() != null ? invoice.getPaymentMethod().name() : "-";
        String status = invoice.getStatus() != null ? invoice.getStatus().name() : "-";
        String billNumber = notBlank(invoice.getBillNumber()) ? invoice.getBillNumber() : "N/A";

        // ── Assemble HTML ────────────────────────────────────
        // Using string concatenation intentionally — String.formatted() / String.format()
        // throws UnknownFormatConversionException when the HTML contains CSS like
        // "letter-spacing: 0.5px;" because '%' followed by ';' is an invalid format specifier.
        return "<!DOCTYPE html>\n"
                + "<html lang=\"en\">\n"
                + "<head>\n"
                + "  <meta charset=\"UTF-8\"/>\n"
                + "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n"
                + "  <title>Invoice #" + escHtml(billNumber) + "</title>\n"
                + "  <style>\n"
                + "    @import url('https://fonts.googleapis.com/css2?family=IBM+Plex+Mono:wght@400;600;700&display=swap');\n"
                + "    * { margin: 0; padding: 0; box-sizing: border-box; }\n"
                + "    body {\n"
                + "      font-family: 'IBM Plex Mono', monospace;\n"
                + "      font-size: 11px;\n"
                + "      background: #fff;\n"
                + "      color: #111;\n"
                + "      width: 80mm;\n"
                + "      max-width: 80mm;\n"
                + "      padding: 6mm 4mm;\n"
                + "      -webkit-print-color-adjust: exact;\n"
                + "      print-color-adjust: exact;\n"
                + "    }\n"
                + "    .header { text-align: center; margin-bottom: 6px; }\n"
                + "    .header .company-name { font-size: 15px; font-weight: 700; letter-spacing: 1px; text-transform: uppercase; }\n"
                + "    .header .sub { font-size: 10px; color: #444; line-height: 1.5; }\n"
                + "    .divider-solid { border: none; border-top: 1.5px solid #111; margin: 5px 0; }\n"
                + "    .divider-dash  { border: none; border-top: 1px dashed #888; margin: 5px 0; }\n"
                + "    .meta-table { width: 100%; border-collapse: collapse; font-size: 10px; margin-bottom: 2px; }\n"
                + "    .meta-table td { padding: 1px 0; }\n"
                + "    .meta-table td:last-child { text-align: right; font-weight: 600; }\n"
                + "    .items-table { width: 100%; border-collapse: collapse; font-size: 10.5px; }\n"
                + "    .items-table thead th { font-weight: 700; font-size: 10px; text-transform: uppercase; letter-spacing: 0.5px; padding: 2px 0; }\n"
                + "    .items-table thead th:first-child { text-align: left; }\n"
                + "    .items-table thead th:not(:first-child) { text-align: right; }\n"
                + "    .items-table tbody td { padding: 2px 0; vertical-align: top; }\n"
                + "    .item-name  { text-align: left; width: 42%; word-break: break-word; }\n"
                + "    .item-qty   { text-align: right; width: 12%; }\n"
                + "    .item-price { text-align: right; width: 20%; }\n"
                + "    .item-total { text-align: right; width: 26%; font-weight: 600; }\n"
                + "    .unknown { font-style: italic; color: #555; }\n"
                + "    .totals-table { width: 100%; border-collapse: collapse; font-size: 10.5px; margin-top: 2px; }\n"
                + "    .totals-table td { padding: 1.5px 0; }\n"
                + "    .totals-table td:last-child { text-align: right; }\n"
                + "    .totals-table .grand td { font-size: 13px; font-weight: 700; padding-top: 4px; }\n"
                + "    .badges { display: flex; justify-content: space-between; margin: 6px 0 4px; }\n"
                + "    .badge { font-size: 10px; font-weight: 700; letter-spacing: 0.5px; text-transform: uppercase; border: 1.5px solid #111; padding: 2px 6px; }\n"
                + "    .footer { text-align: center; font-size: 10px; color: #555; margin-top: 8px; line-height: 1.6; }\n"
                + "    .footer .thank-you { font-size: 12px; font-weight: 700; color: #111; text-transform: uppercase; letter-spacing: 1px; }\n"
                + "    @media print { body { padding: 0; } @page { margin: 0; size: 80mm auto; } }\n"
                + "  </style>\n"
                + "</head>\n"
                + "<body>\n"

                + "  <div class=\"header\">\n"
                + "    <div class=\"company-name\">" + escHtml(notBlank(businessDetail.getCompanyName()) ? businessDetail.getCompanyName() : "Your Business") + "</div>\n"
                + "    <div class=\"sub\">" + escHtml(notBlank(businessDetail.getAddress()) ? businessDetail.getAddress() : "") + "</div>\n"
                + "    <div class=\"sub\">" + contactLine + "</div>\n"
                + "  </div>\n"

                + "  <hr class=\"divider-solid\"/>\n"

                + "  <table class=\"meta-table\">\n"
                + "    <tr><td>Bill #</td><td>" + escHtml(billNumber) + "</td></tr>\n"
                + "    <tr><td>Date</td><td>" + printedAt + "</td></tr>\n"
                + "    <tr><td>Status</td><td>" + escHtml(status) + "</td></tr>\n"
                + metaRows
                + "  </table>\n"

                + "  <hr class=\"divider-dash\"/>\n"

                + "  <table class=\"items-table\">\n"
                + "    <thead><tr><th>Item</th><th>Qty</th><th>Rate</th><th>Amt</th></tr></thead>\n"
                + "    <tbody>" + itemRows + "</tbody>\n"
                + "  </table>\n"

                + "  <hr class=\"divider-dash\"/>\n"

                + "  <table class=\"totals-table\">\n"
                + "    <tr><td>Sub Total</td><td>" + fmt(invoice.getSubTotal()) + "</td></tr>\n"
                + discountRow
                + "    <tr class=\"grand\"><td>TOTAL</td><td>" + fmt(invoice.getGrossTotal()) + "</td></tr>\n"
                + "  </table>\n"

                + "  <div class=\"badges\">\n"
                + "    <span class=\"badge\">" + escHtml(paymentMethod) + "</span>\n"
                + "    <span class=\"badge\">" + escHtml(status) + "</span>\n"
                + "  </div>\n"

                + "  <hr class=\"divider-solid\"/>\n"

                + "  <div class=\"footer\">\n"
                + footerExtra
                + "    <div class=\"thank-you\">Thank You!</div>\n"
                + "    <div>Please visit again</div>\n"
                + "  </div>\n"

                + "</body>\n"
                + "</html>";
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static String fmt(BigDecimal value) {
        if (value == null) return "0.00";
        return value.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private static String formatQty(double qty) {
        if (qty == Math.floor(qty)) return String.valueOf((int) qty);
        return String.format("%.2f", qty).replaceAll("0+$", "").replaceAll("\\.$", "");
    }

    private static boolean notBlank(String value) {
        return value != null && !value.isBlank();
    }

    private static String escHtml(String text) {
        if (text == null) return "";
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}


