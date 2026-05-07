package tapm.shulkerlayers.duck;

/**
 * Duck-typing interface implemented by ShulkerBoxBlockEntity (via mixin) so that
 * other mixins (e.g. BlockEntity#applyImplicitComponents) can read/write the
 * layer without needing direct field access across mixin classes.
 */
public interface LayerAware {
	int shulkerlayers$getLayer();

	void shulkerlayers$setLayer(int layer);
}
