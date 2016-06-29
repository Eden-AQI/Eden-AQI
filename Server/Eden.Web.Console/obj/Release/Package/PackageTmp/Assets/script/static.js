var url = window.location.href;
var vc = null;

if (url.indexOf('app=1') == -1) {
    document.getElementById('baseInfo').style.display = '';
    vc = document.getElementById('videoContainer');
    if (vc)
        vc.style.display = '';
    document.getElementById('footer').style.display = '';
    var voiceContainer = document.getElementById('voiceContainer')
    if (voiceContainer)
        voiceContainer.style.display = '';
} 

var voice = document.getElementById('shopVoice');
var voicePlaying = false;
function toggleVoice(sender) {
    if (voicePlaying)
        voice.pause();
    else
        voice.play();
    voicePlaying = !voicePlaying;
    sender.className = 'voice ' + (voicePlaying ? 'voice-playing' : 'voice-stoped');
}

if (vc) {
    var v = document.getElementById('theVideo');
    var mask = document.getElementById('videomask');
    var mask2 = document.getElementById('videomask-2');
    var vh = v.offsetHeight + 20;
    mask.style.height = vh + 'px';
    mask.style.width = v.offsetWidth + 'px';
    mask2.style.height = (vh / 5 * 3) + 'px';
    mask2.style.marginTop = (vh / 5) + 'px';
}
function playVideo() {
    mask.style.display = 'none';
    v.play();
}