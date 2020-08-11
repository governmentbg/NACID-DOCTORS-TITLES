/**
 * 
 */
package com.nacid.db.mail;

import com.nacid.bl.impl.Utils;
import com.nacid.data.mail.MailRecord;
import com.nacid.db.utils.DatabaseService;
import com.nacid.db.utils.StandAloneDataSource;
import org.apache.commons.lang.StringUtils;


import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//import java.util.LinkedList;
//import com.nacid.bl.mail.MailReceiver;

/**
 * @author bocho
 *
 */
public class MailDB extends DatabaseService {
    /**
     * 
     */
    public final static String MAIL_TABLE = "email";
    

    /**
     * 
     * @param ds
     */
    public MailDB(DataSource ds) {
        super(ds);
       
    }
    /**
     * @param mr
     * @return
     */
    public Integer saveMail(MailRecord mr){
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = this.getConnection();
            ps = con.prepareStatement(
                    "INSERT INTO " +
            		"email(in_out, processed, date, sent_from, sent_to, reply_to, subject, body, message_id) " +
            		"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            //ps.setInt(1, mr.getId());
            int arg = 1;
            ps.setInt(arg++, mr.getIn_out());
            ps.setInt(arg++, mr.getProcessed());
            ps.setTimestamp(arg++, mr.getDate() == null ? null : new Timestamp(mr.getDate().getTime()));
            ps.setString(arg++, mr.getSent_from());
            ps.setString(arg++, mr.getSent_to());
            ps.setString(arg++, mr.getReply_to());
            ps.setString(arg++, mr.getSubject());
            ps.setString(arg++, mr.getBody());
            ps.setString(arg++, mr.getMessage_id());
            if (ps.executeUpdate() > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    return id;
                }
            };
            return null;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }finally{
            try {
                if (ps != null) ps.close();
                this.release(con);
            } catch (SQLException se) {
                throw Utils.logException(se);
            }
        }
    }
    /**
     * @param recepient
     * @return
     */
    public List <MailRecord> getMailByRecepient(String recepient){
        if (StringUtils.isEmpty(recepient)) {
            return new ArrayList<MailRecord>();
        }
        List <MailRecord> mailLst = null;
        try {
            mailLst = selectRecords(MailRecord.class, " sent_to like ?", "%"+recepient+"%");
            
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
        return mailLst;
    }
    /**
     * @param sender
     * @return
     */
    public List <MailRecord> getMailBySender(String sender){
        if (StringUtils.isEmpty(sender)) {
            return new ArrayList<MailRecord>();
        }
        List <MailRecord> mailLst = null;
        try {
            mailLst = selectRecords(MailRecord.class, " sent_from like ?", "%"+sender+"%");
            
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
        return mailLst;
    }
    
    public List <MailRecord> getUnprocessedMail(){
        List <MailRecord> mailLst = null;
        try {
            mailLst = selectRecords(MailRecord.class, " processed = ?", 0);
            
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
        return mailLst;
    }

    public static void main(String[] args) {

        MailDB mailDB = new MailDB(new StandAloneDataSource());
        MailRecord rec = new MailRecord(0, 1, 1, new Date(new java.util.Date().getTime()), "a", "b", "b", "c", "d", "aaa");
        System.out.println(mailDB.saveMail(rec));
    }

}
