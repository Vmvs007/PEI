<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output indent="yes"/>
    <xsl:strip-space elements="*"/>
    <xsl:template match="/*">
        <xsl:apply-templates select="*[1]"/>


    </xsl:template>
    <xsl:template match="/*/*[1]">
        <xsl:copy>
            <xsl:copy-of select="@*|node()|following-sibling::*/*"/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>
