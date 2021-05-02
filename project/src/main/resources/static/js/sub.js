/*
프로젝트 : JM LAB. 2021 포트폴리오.
작성자 : 강정민 
최종 업데이트일자 : 2020.12.10

required 라이브러리
: jquery-3.3.1.min.js (https://jquery.com/)
: jquery-migrate1.4.1.min.js (https://jquery.com/download/)
: jquery-ui (https://jqueryui.com/)

IE 하위버전 맞춤
: html5shiv.js  (HTML5태그 사용)
: matchMedia.js (matchMedia기능 사용)
: matchMedia.addListener.js (matchMedia기능 사용)
*/

$(document).ready(function() {
  uiDom();

  //story게시판 hover
  storyBoardDom();
  $board_wrap_li.eq(0).addClass('lately').addClass('line_first');
  $board_wrap_li.eq(1).addClass('line_first');
  $board_wrap_li.eq(2).addClass('line_first');
  $board_wrap_li.find('a').on('mouseenter',function(){
   var st_index = $(this).closest('li').index();
   storayBoardMouseHover(st_index);
  });
 
  $board_wrap_li.find('a').on('mouseleave',function(){
   $board_wrap_li.find('.st_hover').remove();
  });
  

  //story_detail  수정삭제
  $board_redel.on('click',function(){
    $(this).siblings('.btn_redel').addClass(class_on);
  });
 /* $board_redel.on('blur',function(){
    $(this).siblings('.btn_redel').removeClass(class_on);
  });
*/
  $('.detail_write_wrap .img_view li').on('click',function(){
  
  	if( $(this).find('.uploadImg').attr('src') == '/image/no_img.png' ) {
  		alert("등록된 이미지가 없습니다.");
  		return;
  	}
  
  	$(this).siblings('li').find('input').removeAttr('checked');
    $(this).siblings('li').find('.thum').remove();
  	
  	if($(this).find('input').is(':checked')){
	  	$(this).find('input').removeAttr('checked');
	  	$(this).find('.thum').remove();
  	} else {
	  	$(this).find('input').attr('checked','checked');
	  	$(this).find('.thum').remove();
	  	$(this).find('.img').append('<div class="thum">대표</div>');
  	}
  	
  });


});

//uiDom
var class_on;
var $board_wrap,$board_wrap_li,stItem,stItemTmp,class_visible;
var $board_redel;
function uiDom(){
class_on = 'on';
}

function storyBoardDom(){
  $board_wrap = $('.board_wrap');
  $board_wrap_li = $('.board_wrap li');
  stItemTmp = [];
  class_visible = 'visible';

  $board_redel = $('.board_etc .redel');
}
function storayBoardMouseHover(index){
  $board_wrap_li.find('.st_hover').remove();
  story_title = $board_wrap_li.eq(index).find('p').text();
  console.log(story_title);
  stItem = "<p>"+story_title+"</p>";
  for(var i =0;i<3;i++){
    stItem+=stItem;
  }
  stItemTmp = "<div class='st_hover'>"+stItem+"</div>";
  //console.log(stItemTmp);
  
  $board_wrap_li.eq(index).find('a').append(stItemTmp);
}
