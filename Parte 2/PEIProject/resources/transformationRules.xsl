<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:template match="/">
        <CurrencyDetails>
            <CurrencyRateID><xsl:value-of select="/root/CurrencyRateID/text()"/></CurrencyRateID>
            <CurrencyRateDate><xsl:value-of select="/root/CurrencyRateDate/text()"/></CurrencyRateDate>
            <FromCurrencyCode><xsl:value-of select="/root/FromCurrencyCode/text()"/></FromCurrencyCode>
            <ToCurrencyCode><xsl:value-of select="/root/ToCurrencyCode/text()"/></ToCurrencyCode>
            <RateVal><xsl:value-of select="/root/RateVal/text()"/></RateVal>
        </CurrencyDetails>
    </xsl:template>
</xsl:stylesheet>