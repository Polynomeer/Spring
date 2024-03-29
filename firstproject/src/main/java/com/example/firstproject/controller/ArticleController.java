package com.example.firstproject.controller;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.dto.CommentDto;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import com.example.firstproject.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j // 로깅을 위한 애너테이션
public class ArticleController {

    @Autowired // 스프링 부트가 미리 생성해놓은 객체를 가져다가 자동으로 연결
    private ArticleRepository articleRepository;

    @Autowired
    private CommentService commentService;

    @GetMapping("/articles/new")
    public String newArticleForm() {
        return "articles/new";
    }

    @PostMapping("/articles/create")
    public String createArticle(ArticleForm form) {
        log.info("{}", form);

        // 1. DTO를 변환 to Entity
        Article article = form.toEntity();
        log.info("{}", article);

        // 2. Repository에게 Entity를 DB안에 저장하게 한다.
        Article saved = articleRepository.save(article);
        log.info("{}", saved);

        return "redirect:/articles/" + saved.getId();
    }

    @GetMapping("/articles/{id}")
    public String show(@PathVariable Long id, Model model) {
        log.info("id : {}", id);

        // 1. id로 데이터를 가져온다.
        Article article = articleRepository.findById(id).orElse(null);
        List<CommentDto> commentDtos = commentService.comments(id);

        // 2. 가져온 데이터를 모델에 등록한다.
        model.addAttribute("article", article);
        model.addAttribute("commentDtos", commentDtos);

        // 3. 보여줄 페이지를 설정한다.
        return "articles/show";
    }

    @GetMapping("/articles")
    public String index(Model model) {

        List<Article> articles = articleRepository.findAll();
        // 1. 모든 Article을 가져온다.

        // 2. 가져온 Article 묶음을 뷰로 전달한다.
        model.addAttribute("articleList", articles);

        // 3. 뷰 페이지를 설정한다.
        return "articles/index";
    }

    @GetMapping("/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        // 수정할 데이터를 가져온다.
        Article article = articleRepository.findById(id).orElse(null);

        // 모델에 데이터를 등록한다.
        model.addAttribute("article", article);

        // 뷰 페이지 설정
        return "articles/edit";
    }

    @PostMapping("/articles/update")
    public String update(ArticleForm form) {
        log.info(form.toString());

        // 1. DTO를 엔티티로 변환한다.
        Article article = form.toEntity();
        log.info(article.toString());

        // 2. 엔티티를 DB로 저장한다.

        // 2-1. DB에서 기존 데이터를 가져온다.
        Article target = articleRepository.findById(article.getId()).orElse(null);

        // 2-2. 기존 데이터에 값을 갱신한다.
        if (target != null) {
            articleRepository.save(article);
        }

        // 3. 수정 결과 페이지로 리다이렉트 한다.
        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("/articles/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        log.info("Delete requested");

        // 1. 삭제 대상을 가져온다.
        Article target = articleRepository.findById(id).orElse(null);
        log.info("{}", target);

        // 2. 대상을 삭제한다.
        if (target != null) {
            articleRepository.delete(target);
            attributes.addFlashAttribute("msg", "Delete completed!");
        }

        // 3. 결과 페이지로 리다이렉트 한다.
        return "redirect:/articles";
    }
}
