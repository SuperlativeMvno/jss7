/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * @author baranowb
 * 
 */
public class ResultSourceDiagnosticImpl implements ResultSourceDiagnostic {

	private DialogServiceProviderType providerType;
	private DialogServiceUserType userType;

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.asn.ResultSourceDiagnostic#
	 * getDialogServiceProviderType()
	 */
	public DialogServiceProviderType getDialogServiceProviderType() {

		return providerType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.asn.ResultSourceDiagnostic#
	 * getDialogServiceUserType()
	 */
	public DialogServiceUserType getDialogServiceUserType() {

		return userType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.asn.ResultSourceDiagnostic#
	 * setDialogServiceProviderType
	 * (org.mobicents.protocols.ss7.tcap.asn.DialogServiceProviderType)
	 */
	public void setDialogServiceProviderType(DialogServiceProviderType t) {
		this.providerType = t;
		this.userType = null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.asn.ResultSourceDiagnostic#
	 * setDialogServiceUserType
	 * (org.mobicents.protocols.ss7.tcap.asn.DialogServiceUserType)
	 */
	public void setDialogServiceUserType(DialogServiceUserType t) {
		this.userType = t;
		this.providerType = null;

	}

	
	public String toString() {
		return "ResultSourceDiagnostic[providerType=" + providerType + ", userType=" + userType + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols
	 * .asn.AsnInputStream)
	 */
	public void decode(AsnInputStream ais) throws ParseException {
		int len;
		try {
			len = ais.readLength();

			if (len > ais.available()) {
				throw new ParseException("Not enough data.");
			}

			// int make read whole thing?
			int tag = ais.readTag();
			if (tag == _TAG_U) {
				len = ais.readLength();
				if (len > ais.available()) {
					throw new ParseException("Not enough data.");
				}
				tag = ais.readTag();
				if(tag != Tag.INTEGER)
				{
					throw new ParseException("Expected Integer tag, found: "+tag);
				}
				long t = ais.readInteger();
				this.userType = DialogServiceUserType.getFromInt(t);
				return;
			}

			if (tag == _TAG_P) {
				len = ais.readLength();
				if (len > ais.available()) {
					throw new ParseException("Not enough data.");
				}
				tag = ais.readTag();
				if(tag != Tag.INTEGER)
				{
					throw new ParseException("Expected Integer tag, found: "+tag);
				}
				long t = ais.readInteger();
				this.providerType = DialogServiceProviderType.getFromInt(t);
				return;
			}
			throw new ParseException("Expected on of Diagnostic tags, found: " + tag);
		} catch (IOException e) {
			throw new ParseException(e);
		} catch (AsnException e) {
			throw new ParseException(e);
		}

		// tag can have on of two values =
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.Encodable#encode(org.mobicents.protocols
	 * .asn.AsnOutputStream)
	 */
	public void encode(AsnOutputStream aos) throws ParseException {
		if (this.userType == null && this.providerType == null) {
			throw new ParseException("Value not set");
		}
		try {
			AsnOutputStream localAos = new AsnOutputStream();
			byte[] data = null;
			if (this.userType != null) {
				localAos.writeInteger(this.userType.getType());
				data = localAos.toByteArray();
				localAos.reset();
				localAos.writeTag(_TAG_U_CLASS, _TAG_U_PC_PRIMITIVE, _TAG_U);
				localAos.writeLength(data.length);
				localAos.write(data);
				data = localAos.toByteArray();
				localAos.reset();

			} else {
				localAos.writeInteger(this.providerType.getType());
				data = localAos.toByteArray();
				localAos.reset();
				localAos.writeTag(_TAG_P_CLASS, _TAG_P_PC_PRIMITIVE, _TAG_P);
				localAos.writeLength(data.length);
				localAos.write(data);
				data = localAos.toByteArray();
				localAos.reset();
			}

			aos.writeTag(_TAG_CLASS, _TAG_PC_PRIMITIVE, _TAG);
			aos.writeLength(data.length);

			aos.write(data);
		} catch (IOException e) {
			throw new ParseException(e);
		}

	}

}
