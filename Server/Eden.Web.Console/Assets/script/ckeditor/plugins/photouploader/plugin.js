// Register the plugin within the editor.
CKEDITOR.plugins.add('photouploader', {

    // Register the icons. They must match command names.
    icons: 'photouploader',

    // The plugin initialization logic goes inside this method.
    init: function (editor) {

        // Define the editor command that inserts a timestamp.
        //editor.addCommand('insertPhoto', {

        //    // Define the function that will be fired when the command is executed.
        //    exec: function (editor) {
        //        var now = new Date();
                
        //        // Insert the timestamp into the document.
        //        editor.insertHtml('The current date and time is: <em>' + now.toString() + '</em>');
        //    }
        //});

        editor.addCommand('insertPhoto', new CKEDITOR.dialogCommand('photoDialog'));

        // Create the toolbar button that executes the above command.
        editor.ui.addButton('PhotoUploader', {
            label: '插入图片',
            command: 'insertPhoto',
            toolbar: 'insert'
        });
    }
});

CKEDITOR.dialog.add('photoDialog', function (editor) {
    return {
        title: 'Abbreviation Properties',
        minWidth: 400,
        minHeight: 200,
        contents: [
            {
                id: 'tab1',
                label: 'Basic Settings',
                elements: [
                    {
                        type: 'file',
                        id: 'theFile',
                        label: 'Abbreviation',
                        onSelect: function (fileUrl, errorMessage) {
                            alert('The url of uploaded file is: ' + fileUrl + '\nerrorMessage: ' + errorMessage);
                            // Do not call the built-in onSelect command.
                            // return false;
                        }
                    }, {
                        type: 'fileButton',
                        label: editor.lang.common.uploadSubmit,
                        id: 'id3',
                        filebrowser: {
                            action: 'QuickUpload',
                            params: { type: 'Files', currentFolder: '/folder/' },
                            target: 'tab1:theFile',
                            onSelect: function (fileUrl, errorMessage) {
                                alert('The url of uploaded file is: ' + fileUrl + '\nerrorMessage: ' + errorMessage);
                                // Do not call the built-in onSelect command.
                                // return false;
                            }
                        },
                        'for': ['tab1', 'theFile']
                    }
                ]
            }
        ],

        onShow: function () {
            //var selection = editor.getSelection();
            //var element = selection.getStartElement();

            //if (element)
            //    element = element.getAscendant('abbr', true);

            //if (!element || element.getName() != 'abbr') {
            //    element = editor.document.createElement('abbr');
            //    this.insertMode = true;
            //}
            //else
            //    this.insertMode = false;

            //this.element = element;
            //if (!this.insertMode)
            //    this.setupContent(this.element);
        },

        onOk: function () {
            var dialog = this;
            var abbr = this.element;
            this.commitContent(abbr);

            if (this.insertMode)
                editor.insertElement(abbr);
        }
    };
});