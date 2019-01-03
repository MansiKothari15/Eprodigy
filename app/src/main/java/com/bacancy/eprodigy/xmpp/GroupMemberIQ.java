package com.bacancy.eprodigy.xmpp;

import org.jivesoftware.smack.packet.IQ;

import java.util.ArrayList;

public class GroupMemberIQ extends IQ {

    /*<iq to='eprodigylxcfvvdltk1546324952@localhost/62889680164406418341868400'
    from='live_1546411983@conference.localhost' id='dUs17-43' type='result'>
    <query xmlns='http://jabber.org/protocol/muc#admin'>
    <item affiliation='member' jid='eprodigyl1m3iq6tnv1545809249@localhost'><reason></reason></item></query></iq>*/

    public static final String ELEMENT = "query";
    public static final String NAMESPACE = "xmlns='http://jabber.org/protocol/muc#admin";
    private final String  jId="";

    ArrayList<String> memStringArrayList=new ArrayList<>();

    public GroupMemberIQ(IQ iq) {
        super(iq);
    }

    protected GroupMemberIQ(String childElementName) {
        super(childElementName);
    }


    protected GroupMemberIQ(String childElementName, String childElementNamespace) {
        super(childElementName, childElementNamespace);
    }


    public String getjId() {
        return jId;
    }

    public ArrayList<String> getMemStringArrayList() {
        return memStringArrayList;
    }

    public void setMemStringArrayList(ArrayList<String> memStringArrayList) {
        this.memStringArrayList = memStringArrayList;
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.rightAngleBracket();

        xml.append("<item jid=\"").escape(jId).append("\"/>");

        memStringArrayList.add(jId);

        return null;
    }
}
