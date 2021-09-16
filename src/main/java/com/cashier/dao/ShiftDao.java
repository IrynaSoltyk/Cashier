package com.cashier.dao;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.RequestEntity;
import com.cashier.models.Shift;
import com.cashier.service.ConnectionPool;

public class ShiftDao {
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	private ShiftDao() {}
	
	public static void create(Shift shift) throws UnsuccessfulRequestException {
		String sql = "INSERT INTO SHIFTS (USER_ID, BEGIN_DATE) VALUES (?,?)";
		ConnectionPool cp = ConnectionPool.getInstance();

		try (Connection con = cp.getConnection();
				PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
		st.setInt(1, shift.getUserId());
		st.setTimestamp(2, Timestamp.from(Instant.now()));
		st.executeUpdate();
		try (ResultSet rs = st.getGeneratedKeys()) {
			if (!rs.next()) {
				throw new UnsuccessfulRequestException("Unsuccessfull 'create' request");
			}
			int id = rs.getInt("ID");

			shift.setId(id);
		}
	} catch (SQLException e) {
		logger.error("Failed to create shift", e);
		throw new UnsuccessfulRequestException("Unsuccessfull 'create' request", e.getCause());
	}
	}
	
	public static void closeShift(int id) throws UnsuccessfulRequestException {
		String sql = "UPDATE SHIFTS SET END_DATE = ? WHERE ID = ?";
		ConnectionPool cp = ConnectionPool.getInstance();

		try (Connection con = cp.getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {
			
			int k = 0;
			st.setTimestamp(++k, Timestamp.from(Instant.now()));
			st.setInt(++k, id);
			st.execute();
		
	} catch (SQLException e) {
		logger.error("Failed to close shift", e);
		throw new UnsuccessfulRequestException("Unsuccessfull 'close' request", e.getCause());
	}
	}
	
	public static Shift getShift(int id) throws UnsuccessfulRequestException {
		Shift result = new Shift();
		result.setId(id);
		String sql = "SELECT * FROM SHIFTS WHERE ID = ?";
		ConnectionPool cp = ConnectionPool.getInstance();

		try (Connection con = cp.getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {
			st.setInt(1, id);
			try (ResultSet rs = st.executeQuery()) {
				if (!rs.next()) {
					return null;
				}
				
				Instant beginDate = rs.getTimestamp("BEGIN_DATE").toInstant();
				Instant endDate = rs.getTimestamp("END_DATE").toInstant();
				int userId = rs.getInt("USER_ID");
				
				result.setBeginDate(beginDate);
				result.setUserId(userId);
				if (endDate != null) {
				result.setEndDate(endDate);	
				}
				}
			
		} catch (SQLException e) {
			logger.error("Failed to get shift", e);
			throw new UnsuccessfulRequestException("Unsuccessfull 'Get' request", e.getCause());
		}
		return result;
	}

	public static RequestEntity getAllShifts(int limit , int offset) throws UnsuccessfulRequestException {
		RequestEntity result = new RequestEntity();
		List<Shift> objects = new ArrayList<>();
		
		String countSql =  "SELECT COUNT(ID) FROM SHIFTS";
		String sql = "SELECT * FROM SHIFTS ORDER BY BEGIN_DATE DESC LIMIT ? OFFSET ?";
		ConnectionPool cp = ConnectionPool.getInstance();

		try (Connection con = cp.getConnection();
				Statement countSt = con.createStatement();
				PreparedStatement st = con.prepareStatement(sql)) {
				
			try(ResultSet rs = countSt.executeQuery(countSql)){
				if(!rs.next()) {
					return null;
				}
				result.setCount(rs.getInt(1));
			}
					
			int k = 0;
			st.setInt(++k, limit);
			st.setInt(++k, offset);
			
			try (ResultSet rs = st.executeQuery()) {
				while (rs.next()) {
					Shift shift = new Shift();			
					int id = rs.getInt("ID");
					Instant beginDate = rs.getTimestamp("BEGIN_DATE").toInstant();
					Instant endDate = null;
					if(rs.getTimestamp("END_DATE")!= null) {
						endDate = rs.getTimestamp("END_DATE").toInstant();
					}
					int userId = rs.getInt("USER_ID");
					
					shift.setId(id);
					shift.setBeginDate(beginDate);
					shift.setUserId(userId);
					if (endDate != null) {
						shift.setEndDate(endDate);	
					}
					objects.add(shift);
				}
				result.setObjects(objects);
			}
			
		} catch (SQLException e) {
			logger.error("Failed to get all shifts", e);
			throw new UnsuccessfulRequestException("Unsuccessfull 'Get all' request", e.getCause());
		}
		return result;
	}
	
	public static int getCurrentShiftId() throws UnsuccessfulRequestException {
		String sql = "SELECT ID FROM SHIFTS WHERE END_DATE IS NULL";
		ConnectionPool cp = ConnectionPool.getInstance();

		try (Connection con = cp.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql)) {
			if(!rs.next()) {
				return -1; // no open shifts
			}
			return rs.getInt(1);
		
	} catch (SQLException e) {
		logger.error("Failed to get current shift", e);
		throw new UnsuccessfulRequestException("Unsuccessfull 'get current shift' request", e.getCause());
	}
	}
	
	
}
