/*
 * Copyright 2016 Advanced Media Workflow Assocation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * $Log: PackageID.java,v $
 * Revision 1.5  2011/02/14 22:32:49  vizigoth
 * First commit after major sourceforge outage.
 *
 * Revision 1.4  2011/01/05 13:09:06  vizigoth
 * Created new forge for making record and union type values.
 *
 * Revision 1.3  2011/01/04 10:40:23  vizigoth
 * Refactor all package names to simpler forms more consistent with typical Java usage.
 *
 * Revision 1.2  2009/05/14 16:15:24  vizigoth
 * Major refactor to remove dependency on JPA and introduce better interface and implementation separation. Removed all setPropertiesFromInterface and castFromInterface methods.
 *
 * Revision 1.1  2009/03/30 09:05:00  vizigoth
 * Refactor to use SMPTE harmonized names and add early KLV file support.
 *
 * Revision 1.7  2008/10/16 16:52:01  vizigoth
 * First early release 0.1.
 *
 * Revision 1.6  2008/02/08 12:42:29  vizigoth
 * Comment linking fix.
 *
 * Revision 1.5  2008/02/08 11:34:21  vizigoth
 * Consistent referal to zero rather than null/nil mob id and isOriginal/Contextual methods added to the interface.
 *
 * Revision 1.4  2008/01/10 17:19:30  vizigoth
 * Minor comment fix.
 *
 * Revision 1.3  2007/12/14 15:01:49  vizigoth
 * Added to and edited document comments to a release level.
 *
 * Revision 1.2  2007/12/04 09:46:29  vizigoth
 * Minor formating changes.
 *
 * Revision 1.1  2007/11/13 22:12:55  vizigoth
 * Public release of MAJ API.
 */

package tv.amwa.maj.record;

import tv.amwa.maj.enumeration.MaterialType;
import tv.amwa.maj.exception.GenerationMethodNotSupportedException;
import tv.amwa.maj.exception.InstanceOverflowException;
import tv.amwa.maj.integer.UInt32;
import tv.amwa.maj.integer.UInt8;
import tv.amwa.maj.union.SourceReferenceValue;

/** 
 * <p>Specifies a 32&nbsp;byte <em>package identifier</em> unique identifier that can hold a SMPTE UMID, 
 * as specified in SMPTE&nbsp;330M. Bytes&nbsp;14 to&nbsp;32 of a UMID can be treated as a dumb number, the 
 * value of which has no intrinsic meaning.</p>
 * 
 * <p>A package identifer is globally unique. Please see section&nbsp;4.3.1 of the 
 * <a href="http://www.amwa.tv/html/specs/aafobjectspec-v1.1.pdf">AAF object specification v1.1</a>
 * for details of the immutability of package ids.</p>
 * 
 * <p>Note that the description of byte positions used in the documentation for each method of
 * this interface use 1-based indexing.</p>
 * 
 * <p>To make values of this type, use the following methods from the 
 * {@linkplain tv.amwa.maj.industry.Forge MAJ forge}:</p>
 * 
 * <ul>
 *  <li>From its underlying bytes: {@link tv.amwa.maj.industry.Forge#makePackageID(byte[])};</li>
 *  <li>From its constituent parts: 
 *  {@link tv.amwa.maj.industry.Forge#makePackageID(byte[], byte, byte, byte, byte, AUID)};</li>
 *  <li>According to a specific generation strategy: 
 *  {@link tv.amwa.maj.industry.Forge#generatePackageID(MaterialType, InstanceNumberGeneration, MaterialNumberGeneration)},
 *  {@link tv.amwa.maj.industry.Forge#generatePackageID(MaterialType, InstanceNumberGeneration, MaterialNumberGeneration, AUID)} and
 *  {@link tv.amwa.maj.industry.Forge#generatePackageID(MaterialType, InstanceNumberGeneration, MaterialNumberGeneration, byte[])};</li>
 *  <li>A D-Cinema UMID: {@link tv.amwa.maj.industry.Forge#dCinemaUMID()};</li>
 *  <li>From a URN string representation as generated by {@link #toString()}: 
 *  {@link tv.amwa.maj.industry.Forge#parsePackageID(String)}.</li>
 * </ul>
 * 
 * <p><em>Package ids</em> were previously known as <em>mob ids</em> (media object ids) and may
 * still be referred to that way in AAF-specific documentation.</p> 
 * 
 * @see tv.amwa.maj.industry.TypeDefinitions#PackageIDType
 * @see tv.amwa.maj.misctype.PackageIDType
 * @see AUID
 * 
 * @author <a href="mailto:richard@portability4media.com">Richard Cartwright</a>
*/

public interface PackageID { 

	/** <p>Base universal label to use to create SMPTE-compliant package identifiers.
	 *  This value represents a UMID value with material type and generation methods
	 *  not identified.</p> */
	public final static byte[] BaseUniversalLabel = new byte[] {
			0x06, // Object identifier - universal label start
		 	0x0A, // Label size - 12-byte universal label
		 	0x2B, // Designation: ISO - ISO registered
		 	0x34, // Designation: SMPTE - SMPTE registered
		 	0x01, // Registry categories - Dictionaries
		 	0x01, // Specific categories - Metadata dictionaries
		 	0x01, // Structure - Dictionary standard (SMPTE 335M)
		 	0x05, // Version of the metadata dictionary (defined in SMPTE RP 210)
		 	0x01, // Class - Identifiers and locators
		 	0x01, // Subclass - Globally unique identifiers
		 	MaterialType.NotIdentified.getMaterialTypeCode(), // Material type - not identified
		 	(byte) (MaterialNumberGeneration.NotDefined.getMethodCode() << 4 | 
		 		InstanceNumberGeneration.NotDefined.getMethodCode()),
	};
	
	/**
	 * <p>Returns the universal label that makes up bytes&nbsp;1 to&nbsp;12 of a package id.</p>
	 *
	 * @return Universal label component of the package id.
	 */
	public @UInt8 byte[] getUniversalLabel();

	/**
	 * <p>Sets the universal label that makes up bytes&nbsp;1 to&nbsp;12 of a package id.</p>
	 *
	 * @param label Universal label component of the package id.
	 * 
	 * @throws NullPointerException The given universal label for this package id is <code>null</code>.
	 */
	public void setUniversalLabel(
			@UInt8 byte[] label)
		throws NullPointerException;

	/**
	 * <p>Returns the length of the package id in terms of the number of bytes following this byte, as stored at
	 * byte&nbsp;13 of the package id. This value is normally&nbsp;19 (<code>0x13</code>) for a 32&nbsp;byte package id.</p>
	 *
	 * @return Length component of the package id.
	 */
	public @UInt8 byte getLength();

	/**
	 * <p>Returns the length of the package id in terms of the number of bytes following this byte, as stored at
	 * byte&nbsp;13 of the package id. This value is normally&nbsp;19 (<code>0x13</code>) for a 32&nbsp;byte package id.</p>
	 *
	 * @param length Length component of the package id.
	 */
	public void setLength(
			@UInt8 byte length);

	/**
	 * <p>Returns the most significant bit of the 3&nbsp;byte instance number, which is stored as bytes&nbsp;14 
	 * to&nbsp;16 of the package id. This value is the 14th byte of the package id.</p>
	 *
	 * @return Most significant byte of the instance number component of the package id.
	 */
	public @UInt8 byte getInstanceHigh();

	/**
	 * <p>Sets the most significant bit of the 3&nbsp;byte instance number, which is stored as bytes&nbsp;14 
	 * to&nbsp;16 of the package id. This value is the 14th byte of the package id.</p>
	 *
	 * @param instanceHigh Most significant byte of the instance number component of the package id.
	 */
	public void setInstanceHigh(
			@UInt8 byte instanceHigh);

	/**
	 * <p>Returns the middle byte of the 3&nbsp;byte instance number, which is stored as bytes&nbsp;14 
	 * to&nbsp;16 of the package id. This value is the 15th byte of the package id.</p>
	 *
	 * @return Middle byte of the instance number component of the package id.
	 */
	public @UInt8 byte getInstanceMid();

	/**
	 * <p>Sets the middle byte of the 3&nbsp;byte instance number, which is stored as bytes&nbsp;14 
	 * to&nbsp;16 of the package id. This value is the 15th byte of the package id.</p>
	 *
	 * @param instanceMid Middle byte of the instance number component of the package id.
	 */
	public void setInstanceMid(
			@UInt8 byte instanceMid);

	/**
	 * <p>Returns the least significant byte of the 3 byte instance number, which is stored as bytes&nbsp;14 to&nbsp;16 
	 * of the package id. This value is the 16th byte of the package id.</p>
	 *
	 * @return Least significant byte of the instance number component of the package id.
	 */
	public @UInt8 byte getInstanceLow();

	/**
	 * <p>Sets the least significant byte of the 3&nbsp;byte instance number, which is stored as bytes&nbsp;14 
	 * to&nbsp;16 of the package id. This value is the 16th byte of the package id.</p></p>
	 *
	 * @param instanceLow Least significant byte of the instance number component of the package id.
	 */
	public void setInstanceLow(
			@UInt8 byte instanceLow);

	/**
	 * <p>Returns the material number of the package id, represented as an {@linkplain AUID}, which is taken from 
	 * bytes&nbsp;17 to&nbsp;32 of the package id.</p>
	 *
	 * @return Material number component of the package id.
	 */
	public AUID getMaterial();

	/**
	 * <p>Sets the material number of the package id, represented as an {@linkplain AUID}, which is taken from 
	 * bytes&nbsp;17 to&nbsp;32 of the package id.</p>
	 *
	 * @param material Material number component of the package id
	 * 
	 * @throws NullPointerException The given material number is <code>null</code>.
	 */
	public void setMaterial(
			AUID material)
		throws NullPointerException;
	
	
	/**
	 * <p>Returns <code>true</code> if this package identifier the special zero package identifier, where all 
	 * 32-bytes of the identifier are zero; otherwise <code>false</code>. The zero package identifier
	 * is represented as a URN as follows:</p>
	 * 
	 * <p><center>
	 * <code>urn:x-umid:000000000000000000000000-00-000000-00000000000000000000000000000000</code>
	 * </center></p>
	 * 
	 * <p>Zero package ids are used to specify original source references for {@linkplain tv.amwa.maj.model.SourceClip source 
	 * clips}, indicating that a {@linkplain tv.amwa.maj.model.Package package} represents the original source of essence or data.</p>
	 * 
	 * @return Is the package identifier the special zero package identifier?
	 * 
	 * @see tv.amwa.maj.industry.Forge#zeroPackageID()
	 * @see SourceReferenceValue#isOriginalSource()
	 * @see tv.amwa.maj.model.SourceReferenceSegment#getSourcePackageID()
	 * @see AUID#isNil()
	 */
	public boolean isZero();
	
	/**
	 * <p>Returns a new package identifier with the same material number as this one and a newly created instance
	 * number. The method of generation of the instance number is set as part of the package identifier's universal
	 * label and can be found by calling {@link #getInstanceGenerator()}.</p>
	 * 
	 * <p>The package identifiers generated by this method are only unique within the local context and are not thread 
	 * safe where clones exist or safe for use across more than one virtual machine. It is up to a user
	 * of this method to ensure instance uniqueness in their own local context, using techniques such as
	 * transaction management and persistence mapping to a central store.</p>
	 * 
	 * <p>The relationship between the current and new instance number for each type of generator is:</p>
	 * 
	 * <ul>
	 *  <li>{@link InstanceNumberGeneration#LocalRegistration} - The instance high, mid and low bytes 
	 *  are treated as the most significant, middle and least significant bytes of an unsigned 24-bit 
	 *  integer value representing the local registration number of the package id for the given material 
	 *  number. The instance number of the new package id is set to be one greater than current instance 
	 *  number, as returned by {@link #getInstanceNumber()}.</li>
	 *  <li>{@link  InstanceNumberGeneration#CopyAndPseudoRandom16Bit} - The instance high byte is 
	 *  incremented by one to make the instance high byte of the new package id. If the counter exceeds 
	 *  <code>255</code>, an {@link InstanceOverflowException} is thrown, which gives the caller the 
	 *  option of resetting the counter to <code>0</code> or to take their own choice of action. The 
	 *  middle and low instance bytes are set using a random number generator. This implementation
	 *  ensures that any two consecutive current and new instances do not have the same middle and low 
	 *  instance values.</li>
	 *  <li>{@link InstanceNumberGeneration#PseudoRandom24Bit} - The instance high, middle and low 
	 *  values are set using 24 randomly generated bits. The method ensures that any two consecutive
	 *  current and new instance numbers do not have the same values. </li>
	 *  <li>{@link InstanceNumberGeneration#LiveStream} - Not supported by the MAJ API and throws a 
	 *  {@link GenerationMethodNotSupportedException}.</li>
	 *  <li>{@link InstanceNumberGeneration#NotDefined} - Same as {@link InstanceNumberGeneration#PseudoRandom24Bit}.</li>
	 * </ul>
	 * 
	 * @return The next package id instance as defined by the instance generation method for this package identifier.
	 * 
	 * @throws InstanceOverflowException For methods that increment counters to define the next instance
	 * package id, the counter has overflowed.
	 * @throws GenerationMethodNotSupportedException The instance number generation method is not supported,
	 * which is currently the case for the live stream method.
	 * 
	 * @see #getInstanceGenerator()
	 * @see #getInstanceNumber()
	 * @see #getInstanceHigh()
	 * @see #getInstanceMid()
	 * @see #getInstanceLow()
	 */
	public PackageID nextInstance()
		throws InstanceOverflowException, 
			GenerationMethodNotSupportedException;
	
	/**
	 * <p>Create a cloned copy of this package id.</p>
	 * 
	 * @return Cloned copy of this package id.
	 */
	public PackageID clone();
	
	/** 
	 * <p>Formats the value of this package identifier as a URN-style UMID string, starting with 
	 * "<code>urn:smpte:umid:</code>". For example:</p>
	 *
	 * <p><center><code>urn:smpte:umid:060a2b34.01010101.01010f13.1300b347.53b933d9.18245095.ca82322e.c0a801ba</code></center></p>
	 * 
	 * <p>Values from this method can be turned back into package identifiers using the 
	 * {@link tv.amwa.maj.industry.Forge#parsePackageID(String)} method.<p>
	 * 
	 * <p>The canonical form of formatted values created by this implementation uses
	 * lower case letters for hexadecimal digits.</p>
	 * 
	 * @return The value of this package id formatted as a URN-style UMID string.
	 * 
	 * @see tv.amwa.maj.industry.Forge#parsePackageID(String)
	 */
	public String toString();
	
	/**
	 * <p>Returns the instance number generation method for this package identifier.</p>
	 *
	 * @return Instance number generation method for this package identifier.
	 * 
	 * @see tv.amwa.maj.industry.Forge#generatePackageID(MaterialType, InstanceNumberGeneration, MaterialNumberGeneration)
	 * @see tv.amwa.maj.industry.Forge#generatePackageID(MaterialType, InstanceNumberGeneration, MaterialNumberGeneration, AUID)
	 * @see tv.amwa.maj.industry.Forge#generatePackageID(MaterialType, InstanceNumberGeneration, MaterialNumberGeneration, byte[])
	 * @see #nextInstance()
	 */
	public InstanceNumberGeneration getInstanceGenerator();
	
	/**
	 * <p>Returns the material number generation method for this package identifier.</p>
	 *
	 * @return Material number generation method for this package identifier.
	 * 
	 * @see tv.amwa.maj.industry.Forge#generatePackageID(MaterialType, InstanceNumberGeneration, MaterialNumberGeneration)
	 * @see tv.amwa.maj.industry.Forge#generatePackageID(MaterialType, InstanceNumberGeneration, MaterialNumberGeneration, AUID)
	 * @see tv.amwa.maj.industry.Forge#generatePackageID(MaterialType, InstanceNumberGeneration, MaterialNumberGeneration, byte[])
	 */
	public MaterialNumberGeneration getMaterialNumberGenerator();
	
	/**
	 * <p>Returns the instance number as an unsigned integer made up from its
	 * constituent {@linkplain #getInstanceHigh() high}, {@linkplain #getInstanceMid() middle}
	 * and {@linkplain #getInstanceLow() low} parts. This method is intended for
	 * use with the {@linkplain InstanceNumberGeneration#LocalRegistration local registration
	 * instance number generation strategy}.</p>
	 * 
	 * @return Instance number of this package id represented as an unsigned integer.
	 * 
	 * @see #getInstanceGenerator()
	 */
	public @UInt32 int getInstanceNumber();
}