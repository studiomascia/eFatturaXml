//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2021.03.16 alle 01:10:31 PM CET 
//


package it.studiomascia.gestionale.xml;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per NaturaType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="NaturaType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="N1"/&gt;
 *     &lt;enumeration value="N2"/&gt;
 *     &lt;enumeration value="N2.1"/&gt;
 *     &lt;enumeration value="N2.2"/&gt;
 *     &lt;enumeration value="N3"/&gt;
 *     &lt;enumeration value="N3.1"/&gt;
 *     &lt;enumeration value="N3.2"/&gt;
 *     &lt;enumeration value="N3.3"/&gt;
 *     &lt;enumeration value="N3.4"/&gt;
 *     &lt;enumeration value="N3.5"/&gt;
 *     &lt;enumeration value="N3.6"/&gt;
 *     &lt;enumeration value="N4"/&gt;
 *     &lt;enumeration value="N5"/&gt;
 *     &lt;enumeration value="N6"/&gt;
 *     &lt;enumeration value="N6.1"/&gt;
 *     &lt;enumeration value="N6.2"/&gt;
 *     &lt;enumeration value="N6.3"/&gt;
 *     &lt;enumeration value="N6.4"/&gt;
 *     &lt;enumeration value="N6.5"/&gt;
 *     &lt;enumeration value="N6.6"/&gt;
 *     &lt;enumeration value="N6.7"/&gt;
 *     &lt;enumeration value="N6.8"/&gt;
 *     &lt;enumeration value="N6.9"/&gt;
 *     &lt;enumeration value="N7"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "NaturaType")
@XmlEnum
public enum NaturaType {


    /**
     * Escluse ex. art. 15
     * 
     */
    @XmlEnumValue("N1")
    N_1("N1"),

    /**
     * Non soggette
     * 
     */
    @XmlEnumValue("N2")
    N_2("N2"),

    /**
     * Non soggette ad IVA ai sensi degli artt. da 7 a 7-septies del DPR 633/72
     * 
     */
    @XmlEnumValue("N2.1")
    N_2_1("N2.1"),

    /**
     * Non soggette - altri casi
     * 
     */
    @XmlEnumValue("N2.2")
    N_2_2("N2.2"),

    /**
     * Non imponibili
     * 
     */
    @XmlEnumValue("N3")
    N_3("N3"),

    /**
     * Non Imponibili - esportazioni
     * 
     */
    @XmlEnumValue("N3.1")
    N_3_1("N3.1"),

    /**
     * Non Imponibili - cessioni intracomunitarie
     * 
     */
    @XmlEnumValue("N3.2")
    N_3_2("N3.2"),

    /**
     * Non Imponibili - cessioni verso San Marino
     * 
     */
    @XmlEnumValue("N3.3")
    N_3_3("N3.3"),

    /**
     * Non Imponibili - operazioni assimilate alle cessioni all'esportazione
     * 
     */
    @XmlEnumValue("N3.4")
    N_3_4("N3.4"),

    /**
     * Non Imponibili - a seguito di dichiarazioni d'intento
     * 
     */
    @XmlEnumValue("N3.5")
    N_3_5("N3.5"),

    /**
     * Non Imponibili - altre operazioni che non concorrono alla formazione del plafond
     * 
     */
    @XmlEnumValue("N3.6")
    N_3_6("N3.6"),

    /**
     * Esenti
     * 
     */
    @XmlEnumValue("N4")
    N_4("N4"),

    /**
     * Regime del margine/IVA non esposta in fattura
     * 
     */
    @XmlEnumValue("N5")
    N_5("N5"),

    /**
     * Inversione contabile (per le operazioni in reverse charge ovvero nei casi di autofatturazione per acquisti extra UE di servizi ovvero per importazioni di beni nei soli casi previsti)
     * 
     */
    @XmlEnumValue("N6")
    N_6("N6"),

    /**
     * Inversione contabile - cessione di rottami e altri materiali di recupero
     * 
     */
    @XmlEnumValue("N6.1")
    N_6_1("N6.1"),

    /**
     * Inversione contabile - cessione di oro e argento puro
     * 
     */
    @XmlEnumValue("N6.2")
    N_6_2("N6.2"),

    /**
     * Inversione contabile - subappalto nel settore edile
     * 
     */
    @XmlEnumValue("N6.3")
    N_6_3("N6.3"),

    /**
     * Inversione contabile - cessione di fabbricati
     * 
     */
    @XmlEnumValue("N6.4")
    N_6_4("N6.4"),

    /**
     * Inversione contabile - cessione di telefoni cellulari
     * 
     */
    @XmlEnumValue("N6.5")
    N_6_5("N6.5"),

    /**
     * Inversione contabile - cessione di prodotti elettronici
     * 
     */
    @XmlEnumValue("N6.6")
    N_6_6("N6.6"),

    /**
     * Inversione contabile - prestazioni comparto edile e settori connessi
     * 
     */
    @XmlEnumValue("N6.7")
    N_6_7("N6.7"),

    /**
     * Inversione contabile - operazioni settore energetico
     * 
     */
    @XmlEnumValue("N6.8")
    N_6_8("N6.8"),

    /**
     * Inversione contabile - altri casi
     * 
     */
    @XmlEnumValue("N6.9")
    N_6_9("N6.9"),

    /**
     * IVA assolta in altro stato UE (prestazione di servizi di telecomunicazioni, tele-radiodiffusione ed elettronici ex art. 7-sexies lett. f, g, art. 74-sexies DPR 633/72)
     * 
     */
    @XmlEnumValue("N7")
    N_7("N7");
    private final String value;

    NaturaType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NaturaType fromValue(String v) {
        for (NaturaType c: NaturaType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
