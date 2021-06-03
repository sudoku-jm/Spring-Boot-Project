
 	// 파일 리스트 번호
 	var fileIndex = 0;
 	
 	// 등록할 전체 파일 사이즈
 	var totalFileSize = 0;
 	
 	// 파일 리스트
 	var fileList = new Array();
 	
 	// 파일 사이즈 리스트
 	var fileSizeList = new Array();
 	
 	// 등록 가능한 파일 사이즈MB
 	var uploadSize = 50;
 	
 	// 등록 가능한 총 파일 사이즈 MB
 	var maxUploadSize = 500;
 
 $(function() {
 
 	//파일 드롭 다운
 	fileDropDown();
 
 }); 
 
 
 // 파일 드롭 다운
 function fileDropDown(){
 
 	var dropZone = $(".upload_box");
 	
 	// Drag 기능
 	dropZone.on('dragenter',function(e){
 		e.stopPropagation();	//
 		e.preventDefault();		//
 		
 		//드롭다운 영역 css
 		dropZone.css('background-color', '#8ab6d6');
 	});
 	
 	dropZone.on('dragleave',function(e){ // 진입
 		e.stopPropagation();	//
 		e.preventDefault();		//
 		
 		//드롭다운 영역 css
 		dropZone.css('background-color', '#ffffff');
 	});
 	
 	dropZone.on('dragover',function(e){ // 완전히 들어왔을 때
 		e.stopPropagation();	//
 		e.preventDefault();		//
 		
 		//드롭다운 영역 css
 		dropZone.css('background-color', '#8ab6d6');
 	});
 	
 	dropZone.on('drop',function(e){ // 완전히 들어왔을 때
 		e.preventDefault();		//
 		
 		//드롭다운 영역 css
 		dropZone.css('background-color', '#CAF7E3');
 		
 		var files = e.originalEvent.dataTransfer.files;
        var file = files[0];        // 단일 파일만 가져옴

 		
 		if( files != null && files.length == 1){
 			//selectFile(files);
             selectProfile(file);
 		} else if( files.length > 1){
            alert("단일 이미지 파일만 업로드 가능합니다.");
            return;
         } 
         else{
			alert("업로드가 불가능한 파일입니다.");
			return;
 		}
 		
 	});
 	
 }
 

 
 function selectProfile(file){

    // 파일 이름
    var fileName = file.name;				// 프사.2021.06.png
    var fileNameArr = fileName.split("\.");		// [프사][2021][06][png]
                 
    // 확장자
    var ext = fileNameArr[fileNameArr.length - 1]; 

    // 파일 사이즈(MB)
    var fileSize = (file.size / 1024 / 1024).toFixed(4) ;

    // $.inArray (value, [배열])		:	value가 배열에 있는지 확인하고 true , false로 반환
    if(!$.inArray(ext, ['jpeg' , 'jpg' , 'gif' , 'png'])){
        // 확장자 체크
        alert("이미지 형식 (jpeg , jpg , gif , png) 파일만 업로드 가능합니다.");
        return;
    }else if( fileSize > uploadSize ){
        // 파일 사이즈 체크
        alert("최대" + uploadSize + "MB 까지만 업로드 가능합니다.");
        return;
    }
    
    addProfile(fileName, fileSize , file);
    


   
 }


 function addProfile(fileName, fileSize , file){

    var reader = new FileReader();
    reader.onload = function(e) {
        $("#profile").attr("src", e.target.result);
    }
    reader.readAsDataURL(file);

    $('.upload_box h3').text(fileName);
    $('.upload_box p').text("("+ fileSize + " MB)");


    if( confirm(" 프로필 사진을 변경하시겠습니까? ")){
        // 비동기 요청 바로 변경
    }

    uploadFile(file);

 }
 
 
 function selectFile(files){
 
 	// 다중파일 등록
 	if(files != null){
 		for(var i = 0; i < files.length; i++){
 		
 			// 파일 이름
 			var fileName = files[i].name;				// 프사.2021.06.png
 			var fileNameArr = fileName.split("\.");		// [프사][2021][06][png]
 			 			
 			// 확장자
 			var ext = fileNameArr[fileNameArr.length - 1]; 

			// 파일 사이즈(MB)
			var fileSize = (files[i].size / 1024 / 1024).toFixed(4) ;
			
			// 배열에 특정 값이 있는지 알아내기
			// $.inArray (value, [배열])		:	value가 배열에 있는지 확인하고 true , false로 반환
			if(!$.inArray(ext, ['jpeg' , 'jpg' , 'gif' , 'png'])){
				// 확장자 체크
				alert("이미지 형식 (jpeg , jpg , gif , png) 파일만 업로드 가능합니다.");
				break;
			}else if( fileSize > uploadSize ){
				// 파일 사이즈 체크
				alert("최대" + uploadSize + "MB 까지만 업로드 가능합니다.");
                break;
			} else{
				// 전체 파일 사이즈
				totalFileSize += fileSize;
				
				// 파일 배열에 파일 추가
				fileList[fileIndex] = files[i];
				
				// 파일사이즈 배열에 사이즈 추가
				fileSizeList[fileIndex] = fileSize;
				
				// 업로드 파일 목록 생성
				addFileList(fileIndex, fileName, fileSize);

                // 프로필 추가
				
				// 파일 번호 증가
				fileIndex++;
			}

 		}
 		
 	}else{
 		alert("파일 업로드 중, 에러가 발생하였습니다.");
 	}
 	
 }
 
 // 업로드 파일 목록 생성
 function addFileList(fileIndex, fileName, fileSize){
 
 	var html = "";
 	
 	html += "<li>";
 	html += fileIndex + ". " + fileName + "(" + fileSize + " MB)";
 	html += "</li>";
 
 	$('.file_list').append(html);
 }  
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 