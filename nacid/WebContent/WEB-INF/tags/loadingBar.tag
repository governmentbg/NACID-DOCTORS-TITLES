<script type="text/javascript">
	var progressUpdater;
	function startProgressUpdater() {
		progressUpdater = new Ajax.PeriodicalUpdater('progress-container', '${pathPrefix }/ajax/attachment_ajax', {
			  method: 'get', frequency: 1, decay: 1
			});
	}

	function stopProgresUpdater() {
		progressUpdater.stop();
	}
</script>

<div id="progress-container">
</div>