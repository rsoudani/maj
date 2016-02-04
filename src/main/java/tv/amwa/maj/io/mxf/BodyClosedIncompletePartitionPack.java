package tv.amwa.maj.io.mxf;

/**
 * <p>Represents the description of {@linkplain BodyPartition body partition} that is closed 
 * and incomplete.</p>
 * 
 * <p>A closed partition is one in which required header metadata values have been finalized 
 * and so all required metadata is present and correct. All closed partitions that contain 
 * {@linkplain HeaderMetadata header metadata} shall have identical header metadata.</p>
 * 
 * <p>An incomplete partition is one where {@linkplain HeaderMetadata header metadata} exists and 
 * some best effort metadata properties have been flagged as unknown, by setting to the appropriate 
 * distinguished value.</p>
 * 
 *
 * 
 * @see HeaderClosedIncompletePartitionPack
 * @see FooterClosedIncompletePartitionPack
 * @see BodyClosedCompletePartitionPack
 * @see BodyOpenCompletePartitionPack
 * @see BodyOpenIncompletePartitionPack
 *
 */
public interface BodyClosedIncompletePartitionPack 
	extends 
		BodyPartitionPack,
		Cloneable {

	/**
	 * <p>Create a cloned copy of this body closed incomplete partition pack.</p>
	 *
	 * @return Cloned copy of this body closed incomplete partition pack.
	 */
	public BodyClosedIncompletePartitionPack clone();
}
